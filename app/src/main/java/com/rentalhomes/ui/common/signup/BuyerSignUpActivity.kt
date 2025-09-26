package com.rentalhomes.ui.common.signup

import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.messaging.FirebaseMessaging
import com.rentalhomes.BR
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.R
import com.rentalhomes.data.network.model.requestModel.LoginRequest
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.databinding.ActivityBuyerSignUpBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.ui.buyer.home.BuyerHomeActivity
import com.rentalhomes.ui.common.setting.privacypolicy.PrivacyPolicyActivity
import com.rentalhomes.ui.common.setting.termsconditions.TermsConditionsActivity
import com.rentalhomes.ui.common.signin.SignInActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods
import com.rentalhomes.utils.KeyboardUtils


class BuyerSignUpActivity : BaseActivity<ActivityBuyerSignUpBinding, BuyerSignUpVM>(),
    BuyerSignUpNavigator, View.OnClickListener {

    private lateinit var binding: ActivityBuyerSignUpBinding
    private lateinit var snackBar: View
    private lateinit var buyerSignUpVM: BuyerSignUpVM

    private var slideLeft: Animation? = null
    private var slideRight: Animation? = null

    private var currentView = 0
    private var userSelected: Int = 0
    private var REQUEST_CODE_AUTOCOMPLETE: Int = 0

    private var isPassVisible: Boolean = false //to check password visibility
    private var isConfPassVisible: Boolean = false //to check password visibility

    private var sb: StringBuilder? = null
    private var verifiedEmail: String = "" //to check verified email with changed email
    private var isVerified: Boolean = false //to check email
    private var isEmailChanged: Boolean = false //to check email changed or not
    private lateinit var strOtp: String //to store OTP from OTP box

    companion object {
        private val TAG = BuyerSignUpActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        generateToken()
        init()
        setOnListener()
        setOnEditorIme()
        setSpanString()
    }

    private fun setOnEditorIme() {
        //On pressing ime option done goes to next screen

        //Last Name
        binding.etLName.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (isValidFName() && isValidLName()) {
                    KeyboardUtils.hideKeyboard(this, snackBar)
                    nextSuburb()
                    currentView = 1
                }
                true
            } else {
                KeyboardUtils.hideKeyboard(this, snackBar)
                false
            }
        }

        //Suburb
        binding.etSuburb.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (isValidSuburb()) {
                    KeyboardUtils.hideKeyboard(this, snackBar)
                    nextToMobile()
                    currentView = 2
                }
                true
            } else {
                KeyboardUtils.hideKeyboard(this, snackBar)
                false
            }
        }

        //Mobile Number
        binding.etMobile.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (isValidPhone()) {
                    KeyboardUtils.hideKeyboard(this, snackBar)
                    nextToEmail()
                    currentView = 3
                }
                true
            } else {
                KeyboardUtils.hideKeyboard(this, snackBar)
                false
            }
        }

        //Email Address
        /*binding.etEmail.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (isValidEmail()) {
                    KeyboardUtils.hideKeyboard(this, snackBar)
                    nezxtToPassword()
                    currentView = 4
                }
                true
            } else {
                KeyboardUtils.hideKeyboard(this, snackBar)
                false
            }
        }*/

    }

    private fun init() {
        //Initialization is done here
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)
        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this@BuyerSignUpActivity
            it.buyerSignUpVM = buyerSignUpVM
        }
        buyerSignUpVM.attachContext(this)
        buyerSignUpVM.setNavigator(this)

        //Initialize animation here
        slideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left)
        slideRight = AnimationUtils.loadAnimation(this, R.anim.slide_right)

        //Initialize snackBar object here
        snackBar = findViewById(android.R.id.content)
//        userSelected = intent?.getIntExtra(GlobalFields.USER_SELECTED, 0)!!

    }

    override fun setHeader() {

    }

    override val bindingVariable: Int
        get() = BR.buyerSignUpVM
    override val layoutId: Int
        get() = R.layout.activity_buyer_sign_up
    override val viewModel: BuyerSignUpVM
        get() {
            buyerSignUpVM = ViewModelProvider(this).get(BuyerSignUpVM::class.java)
            return buyerSignUpVM
        }

    override fun setObservers() {
    }

    //setOnListener define here
    private fun setOnListener() {
        binding.ivBack.setOnClickListener(this)
        binding.btnContinue.setOnClickListener(this)
        binding.tvSignIn.setOnClickListener(this)
        binding.ivVisIconPass.setOnClickListener(this)
        binding.ivVisIconConf.setOnClickListener(this)

        //Suburb click
//        binding.etSuburb.setOnClickListener {
//            openPlacePicker()
//        }

        binding.etMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Check and show validation of First Name
                if (!isValidPhone() && p0.toString().isNotEmpty()) {
                    binding.tvValMobile.visibility = View.VISIBLE
                } else {
                    binding.tvValMobile.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (isValidPhone() && p0.toString().isNotEmpty()) {
                    GlobalMethods.enableButton(this@BuyerSignUpActivity, binding.btnContinue)
                } else {
                    GlobalMethods.disableButton(this@BuyerSignUpActivity, binding.btnContinue)
                }

//                val text = binding.etMobile.text.toString()
//                val textLength = binding.etMobile.text!!.length
//
//                if (text.endsWith(" ")) {
//                    return
//                }
//
//                when (textLength) {
//                    4 -> {
//                        setText(StringBuilder(text).insert(text.length - 1, " ").toString())
//                    }
//                    8 -> {
//                        setText(StringBuilder(text).insert(text.length - 1, " ").toString())
//                    }
//                    12 -> {
//                        setText(StringBuilder(text).insert(text.length - 1, " ").toString())
//                    }
//                }
            }

            private fun setText(text: String) {
                binding.etMobile.removeTextChangedListener(this)
                binding.etMobile.editableText.replace(0, binding.etMobile.text!!.length, text)
                binding.etMobile.setSelection(text.length)
                binding.etMobile.addTextChangedListener(this)
            }

        })

        binding.etSuburb.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Check and show validation of First Name
                if (!isValidSuburb() && p0.toString().isNotEmpty()) {
                    binding.tvValSuburb.visibility = View.VISIBLE
                } else {
                    binding.tvValSuburb.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (isValidSuburb()) {
                    GlobalMethods.enableButton(this@BuyerSignUpActivity, binding.btnContinue)
                } else {
                    GlobalMethods.disableButton(this@BuyerSignUpActivity, binding.btnContinue)
                }
            }

        })

        binding.etFName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Check and show validation of First Name
                if (!isValidFName() && p0.toString().isNotEmpty()) {
                    binding.tvValFName.visibility = View.VISIBLE
                } else {
                    binding.tvValFName.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (isValidFName() && isValidLName()) {
                    GlobalMethods.enableButton(this@BuyerSignUpActivity, binding.btnContinue)
                } else {
                    GlobalMethods.disableButton(this@BuyerSignUpActivity, binding.btnContinue)
                }
            }

        })

        binding.etLName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Check and show validation of Last Name
                if (!isValidLName() && p0.toString().isNotEmpty()) {
                    binding.tvValLName.visibility = View.VISIBLE
                } else {
                    binding.tvValLName.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (isValidFName() && isValidLName()) {
                    GlobalMethods.enableButton(this@BuyerSignUpActivity, binding.btnContinue)
                } else {
                    GlobalMethods.disableButton(this@BuyerSignUpActivity, binding.btnContinue)
                }
            }

        })

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Check and show validation of Email
                if (!isValidEmail() && p0.toString().isNotEmpty()) {
                    binding.tvValEmail.visibility = View.VISIBLE
                } else {
                    binding.tvValEmail.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (isValidEmail()) {
//                    binding.tvGetOTP.setTextColor(getColor(R.color.black))
//                    binding.tvGetOTP.isEnabled = true
                    enableOTPButton()

                    if (verifiedEmail != binding.etEmail.text.toString().trim()) {
                        isVerified = false
                        binding.btnContinue.text = getString(R.string.verify_otp)
                        GlobalMethods.disableButton(this@BuyerSignUpActivity, binding.btnContinue)
                        isEmailChanged = true
                        enableOTPButton()
                    } else {
                        isVerified = true
                        binding.btnContinue.text = getString(R.string.btn_continue)
                        GlobalMethods.enableButton(this@BuyerSignUpActivity, binding.btnContinue)
                        isEmailChanged = true
                        disableOPTButton()
                    }
                } else {
//                    binding.tvGetOTP.setTextColor(getColor(R.color.grey_100))
                    disableOPTButton()
                }
            }

        })

        binding.edtOtp1.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
                if (sb.toString().isNotEmpty() && binding.edtOtp1.length() == 1) {
                    sb?.append(s)
                    binding.edtOtp1.clearFocus()
                    binding.edtOtp2.requestFocus()
                } else {
//                    binding.edtOtp1.setBackgroundResource(com.reliefdriver.R.drawable.edittext_curve_bg)
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                if (sb?.length != 1) {
                    sb?.deleteCharAt(0)
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (sb?.length == 0) {
                    binding.edtOtp1.requestFocus()
                }

                if (s.isEmpty()) {
                    GlobalMethods.disableButton(this@BuyerSignUpActivity, binding.btnContinue)
                }
            }
        })

        binding.edtOtp2.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (sb?.length != 0 && binding.edtOtp2.length() == 1) {
                    sb?.append(s)
                    binding.edtOtp2.clearFocus()
                    binding.edtOtp3.requestFocus()
                } else {
//                    binding.edtOtp2.setBackgroundResource(R.drawable.edittext_curve_bg)
                    binding.edtOtp1.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                if (sb?.length != 1) {
                    sb?.deleteCharAt(0)
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (sb?.length == 0) {
                    binding.edtOtp2.requestFocus()
                }

                if (s.isEmpty()) {
                    GlobalMethods.disableButton(this@BuyerSignUpActivity, binding.btnContinue)
                }
            }
        })

        binding.edtOtp3.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
                if (sb?.length != 0 && binding.edtOtp3.length() == 1) {
                    sb?.append(s)
                    binding.edtOtp3.clearFocus()
                    binding.edtOtp4.requestFocus()
                } else {
//                    binding.edtOtp3.setBackgroundResource(R.drawable.edittext_curve_bg)
                    binding.edtOtp2.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                if (sb?.length == 1) {
                    sb?.deleteCharAt(0)
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (sb?.length == 0) {
                    binding.edtOtp3.requestFocus()
                }

                if (s.isEmpty()) {
                    GlobalMethods.disableButton(this@BuyerSignUpActivity, binding.btnContinue)
                }
            }
        })

        binding.edtOtp4.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
                if (sb?.length != 0 && binding.edtOtp4.length() == 1) {
                    sb?.append(s)
                } else {
//                    binding.edtOtp4.setBackgroundResource(R.drawable.edittext_curve_bg)
                    binding.edtOtp3.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                if (sb?.length == 1) {
                    sb?.deleteCharAt(0)
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (sb?.length == 0) {
                    binding.edtOtp3.requestFocus()
                    GlobalMethods.disableButton(this@BuyerSignUpActivity, binding.btnContinue)
                } else {
                    GlobalMethods.enableButton(this@BuyerSignUpActivity, binding.btnContinue)
                }

                if (s.isNotEmpty()) {
                    KeyboardUtils.hideKeyboard(this@BuyerSignUpActivity, binding.edtOtp4)
                } else {
                    GlobalMethods.disableButton(this@BuyerSignUpActivity, binding.btnContinue)
                }
            }
        })

        binding.cbTermsCondition.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isValidPassword() && isValidConfirmPassword() && isChecked) {
                GlobalMethods.enableButton(this@BuyerSignUpActivity, binding.btnContinue)
            } else {
                GlobalMethods.disableButton(this@BuyerSignUpActivity, binding.btnContinue)
            }
        }

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Check and show validation of Password
                if (!isValidPassword() && p0.toString().isNotEmpty()) {
                    binding.tvValPassword.visibility = View.VISIBLE
                } else {
                    binding.tvValPassword.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (isValidPassword() && isValidConfirmPassword() && binding.cbTermsCondition.isChecked) {
                    GlobalMethods.enableButton(this@BuyerSignUpActivity, binding.btnContinue)
                } else {
                    GlobalMethods.disableButton(this@BuyerSignUpActivity, binding.btnContinue)
                }
            }

        })

        binding.etConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Check and show validation of Confirm Password
                if (!isValidConfirmPassword() && p0.toString().isNotEmpty()) {
                    binding.tvValConfirmPassword.visibility = View.VISIBLE
                } else {
                    binding.tvValConfirmPassword.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (isValidPassword() && isValidConfirmPassword() && binding.cbTermsCondition.isChecked) {
                    GlobalMethods.enableButton(this@BuyerSignUpActivity, binding.btnContinue)
                } else {
                    GlobalMethods.disableButton(this@BuyerSignUpActivity, binding.btnContinue)
                }
            }

        })
    }

    override fun onClick(v: View?) {
        v?.let { view ->
            when (view.id) {
                R.id.ivBack -> {
                    KeyboardUtils.hideKeyboard(this, snackBar)
                    if (currentView == 0) {
                        previousToUserSelection()
                    } else if (currentView == 1) {
                        previousToFNameLName()
                    } else if (currentView == 2) {
                        previousToSuburb()
                    } else if (currentView == 3) {
                        previousToMobile()
                    } else if (currentView == 4) {
                        previousToEmail()
                    } /*else if (currentView == 5) {
                        previousToPassword()
                    }*/
                }
                R.id.btnContinue -> {
                    KeyboardUtils.hideKeyboard(this, snackBar)
                    if (currentView == 0) {
                        if (isValidFName() && isValidLName()) {
                            nextSuburb()
                            currentView = 1
                        }
                    } else if (currentView == 1) {
                        if (isValidSuburb()) {
                            nextToMobile()
                            currentView = 2
                        }
                    } else if (currentView == 2) {
                        if (isValidPhone()) {
                            nextToEmail()
                            currentView = 3
                        }
                    } else if (currentView == 3) {
//                        if (isValidEmail()) {
                        if (!isVerified) {
                            if (isValidOTP()) {
                                //OTP Verification API call
                                buyerSignUpVM.otpVerificationAPICall(
                                    binding.etEmail.text.toString().trim(),
                                    2,
                                    Integer.parseInt(strOtp)
                                )
                            }
                        } else {
                            nextToPassword()
                            currentView = 4
                        }
                    } else if (currentView == 4) {
                        if (isValidPassword() && isValidConfirmPassword()) {
                            KeyboardUtils.hideKeyboard(this, snackBar)
                            buyerSignUpVM.signUpCall()
                        }
                    }
                }
                R.id.tvSignIn -> {
                    KeyboardUtils.hideKeyboard(this, snackBar)
                    intent = Intent(this, SignInActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                R.id.ivVisIconPass -> {
                    setPasswordVisibility()
                }
                R.id.ivVisIconConf -> {
                    setConfPassVisibility()
                }
            }
        }
    }

    private fun isValidPhone(): Boolean {
        val phone: String = binding.etMobile.text.toString().trim()

        if (phone.isNotEmpty() && phone.length < 9) {
            binding.tvValMobile.text = resources.getString(R.string.msg_phone_length_min_10_digits)
            return false
        }
        return true
    }

    //Suburb validation
    fun isValidSuburb(): Boolean {
        val suburb: String = binding.etSuburb.text.toString().trim()

        if (TextUtils.isEmpty(suburb)) {
            binding.tvValSuburb.text = resources.getString(R.string.msg_empty_suburb)
            return false
        }

        if (suburb.length < 2) {
            binding.tvValSuburb.text = resources.getString(R.string.msg_suburb_length)
            return false
        }
        return true
    }


    //FirsName validation
    fun isValidFName(): Boolean {
        val fName: String = binding.etFName.text.toString().trim()

        if (TextUtils.isEmpty(fName)) {
            binding.tvValFName.text = resources.getString(R.string.msg_empty_first_name)
            return false
        }

        if (fName.length < 2) {
            binding.tvValFName.text = resources.getString(R.string.msg_first_name_length)
            return false
        }
        return true
    }

    //LastName validation
    fun isValidLName(): Boolean {
        val lName: String = binding.etLName.text.toString().trim()

        if (lName.isEmpty()) {
            binding.tvValLName.text = resources.getString(R.string.msg_empty_last_name)
            return false
        }

        if (lName.length < 2) {
            binding.tvValLName.text = resources.getString(R.string.msg_last_name_length)
            return false
        }
        return true
    }

    //Email validation
    private fun isValidEmail(): Boolean {
        val email: String = binding.etEmail.text.toString().trim()

        if (!GlobalMethods.isValidEmail(email)) {
            binding.tvValEmail.text = resources.getString(R.string.msg_invalid_email)
            return false
        }
        return true
    }

    private fun isValidPassword(): Boolean {
        val password: String = binding.etPassword.text.toString().trim()

        if (password.length < 6) {
            binding.tvValPassword.text = resources.getString(R.string.msg_password_length)
            return false
        }

        if (GlobalMethods.isContainEmoji(password)) {
            binding.tvValPassword.text =
                resources.getString(R.string.msg_password_not_contain_emoji)
            return false
        }
        return true
    }

    private fun isValidConfirmPassword(): Boolean {
        val password: String = binding.etPassword.text.toString().trim()
        val confirmPassword: String = binding.etConfirmPassword.text.toString().trim()

        if (password != confirmPassword) {
            binding.tvValConfirmPassword.text =
                resources.getString(R.string.msg_password_confirm_pass_not_match)
            return false
        }
        return true
    }

    private fun isValidOTP(): Boolean {
        val otp1: String = binding.edtOtp1.text.toString().trim()
        val otp2: String = binding.edtOtp2.text.toString().trim()
        val otp3: String = binding.edtOtp3.text.toString().trim()
        val otp4: String = binding.edtOtp4.text.toString().trim()

        if (otp1.isEmpty() || otp2.isEmpty() || otp3.isEmpty() || otp4.isEmpty()) {
            return false
        }
        strOtp = otp1 + otp2 + otp3 + otp4
        return true
    }

    private fun nextToMobile() {
        binding.clSuburb.visibility = View.GONE
        binding.clMobile.visibility = View.VISIBLE
        binding.ivBack.visibility = View.VISIBLE

        binding.clMobile.startAnimation(slideRight)

        if (binding.etMobile.text.toString().trim().isEmpty() || binding.etMobile.text.toString()
                .trim().length < 9
        ) {
            GlobalMethods.disableButton(
                this@BuyerSignUpActivity,
                binding.btnContinue
            )
        } else {
            GlobalMethods.enableButton(
                this@BuyerSignUpActivity,
                binding.btnContinue
            )
        }
    }

    private fun nextSuburb() {
        binding.clFNameLName.visibility = View.GONE
        binding.clSuburb.visibility = View.VISIBLE
        binding.ivBack.visibility = View.VISIBLE

        binding.clFNameLName.startAnimation(slideRight)
        if (!isValidFName() && !isValidLName()) {
            GlobalMethods.disableButton(
                this@BuyerSignUpActivity,
                binding.btnContinue
            )
        }

    }

    private fun nextToEmail() {
        binding.clMobile.visibility = View.GONE
        binding.clEmail.visibility = View.VISIBLE
        binding.ivBack.visibility = View.VISIBLE

        binding.clEmail.startAnimation(slideRight)

        if (!isValidEmail()) {
            GlobalMethods.disableButton(
                this@BuyerSignUpActivity,
                binding.btnContinue
            )
        }

//        if (!isVerified) {
//            binding.btnContinue.text = getString(R.string.verify_otp)
//        }

        if (isEmailChanged && !isVerified) {
            binding.btnContinue.text = getString(R.string.verify_otp)
            GlobalMethods.disableButton(this@BuyerSignUpActivity, binding.btnContinue)
        }
    }

    private fun nextToPassword() {
        binding.clEmail.visibility = View.GONE
        binding.clPassword.visibility = View.VISIBLE
        binding.ivBack.visibility = View.VISIBLE
        binding.btnContinue.text = getString(R.string.sign_up)
        binding.clPassword.startAnimation(slideRight)

        if (!isValidPassword() || !isValidConfirmPassword() || !binding.cbTermsCondition.isChecked) {
            GlobalMethods.disableButton(
                this@BuyerSignUpActivity,
                binding.btnContinue
            )
        }
        binding.btnContinue.text = getString(R.string.btn_continue)
    }

    /*private fun nextToPromo() {
        binding.clPassword.visibility = View.GONE
        binding.clPromoCode.visibility = View.VISIBLE
        binding.btnContinue.setText(R.string.sign_up)

        binding.clPromoCode.startAnimation(slideRight)

        if (!isValidPromo()) {
            GlobalMethods.disableButton(
                this@BuyerSignUpActivity,
                binding.btnContinue
            )
        }
    }*/

    private fun previousToUserSelection() {
//        onBackPressed()
        super.onBackPressed()
    }

    override fun onBackPressed() {
        KeyboardUtils.hideKeyboard(this, snackBar)
        if (currentView == 0) {
            previousToUserSelection()
        } else if (currentView == 1) {
            previousToFNameLName()
        } else if (currentView == 2) {
            previousToSuburb()
        } else if (currentView == 3) {
            previousToMobile()
        } else if (currentView == 4) {
            previousToEmail()
        }
    }

    /*private fun previousToPassword() {
        binding.clPromoCode.visibility = View.GONE
        binding.clPassword.visibility = View.VISIBLE
        currentView = 4

        binding.clPassword.startAnimation(slideLeft)

        GlobalMethods.enableButton(
            this@BuyerSignUpActivity,
            binding.btnContinue
        )
    }*/

    private fun previousToSuburb() {
        binding.clMobile.visibility = View.GONE
        binding.clSuburb.visibility = View.VISIBLE
        binding.ivBack.visibility = View.VISIBLE
        currentView = 1

        binding.clSuburb.startAnimation(slideLeft)

        GlobalMethods.enableButton(
            this@BuyerSignUpActivity,
            binding.btnContinue
        )
    }


    private fun previousToFNameLName() {
        binding.clSuburb.visibility = View.GONE
        binding.clFNameLName.visibility = View.VISIBLE
        binding.ivBack.visibility = View.VISIBLE
        currentView = 0

        binding.clFNameLName.startAnimation(slideLeft)

        GlobalMethods.enableButton(
            this@BuyerSignUpActivity,
            binding.btnContinue
        )
    }

    private fun previousToMobile() {
        binding.clEmail.visibility = View.GONE
        binding.clMobile.visibility = View.VISIBLE
        currentView = 2

        binding.clMobile.startAnimation(slideLeft)
        binding.btnContinue.setText(R.string.btn_continue)

        GlobalMethods.enableButton(
            this@BuyerSignUpActivity,
            binding.btnContinue
        )
        binding.btnContinue.text = getString(R.string.btn_continue)
    }

    private fun previousToEmail() {
        binding.clPassword.visibility = View.GONE
        binding.clEmail.visibility = View.VISIBLE
        currentView = 3

        binding.clEmail.startAnimation(slideLeft)
        binding.btnContinue.setText(R.string.btn_continue)

        GlobalMethods.enableButton(
            this@BuyerSignUpActivity,
            binding.btnContinue
        )
    }

    private fun setSpanString() {
        val spanText =
            SpannableStringBuilder(getString(R.string.terms_and_conditions))

        binding.tvCBText.highlightColor = getColor(android.R.color.transparent)

        val termsClickSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                intent = Intent(this@BuyerSignUpActivity, TermsConditionsActivity::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        val privacyClickSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                intent = Intent(this@BuyerSignUpActivity, PrivacyPolicyActivity::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        spanText.setSpan(termsClickSpan, 29, 47, 0)
        spanText.setSpan(privacyClickSpan, 50, 64, 0)

        spanText.setSpan(
            ForegroundColorSpan(getColor(R.color.grey_201)),
            29, // start
            47, // end
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spanText.setSpan(
            ForegroundColorSpan(getColor(R.color.grey_201)),
            50, // start
            64, // end
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spanText.setSpan(
            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
            29,
            47,
            0
        )
        spanText.setSpan(
            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
            50,
            64,
            0
        )

        binding.tvCBText.setText(spanText, TextView.BufferType.SPANNABLE)
        binding.tvCBText.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun goToHome() {
        intent = Intent(this, BuyerHomeActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun showProgress() {
        showProgressDialog()
    }

    override fun hideProgress() {
        hideProgressDialog()
    }

    override fun setMessageComingFromServer(it: String) {
        AlertDialogUtils.showSnakeBar(it, snackBar, this)
    }

    private fun openPlacePicker() {
        // Initialize Places.
        Places.initialize(
            this, getString(R.string.google_maps_key)
        )
        // Create a new Places client instance.
        Places.createClient(this)
        // Set the fields to specify which types of place data to return.
        val fields: List<Place.Field> = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS,
            Place.Field.ID,
            Place.Field.PHONE_NUMBER,
            Place.Field.RATING,
            Place.Field.WEBSITE_URI
        )
        // Start the autocomplete intent.
        val intent: Intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN,
            fields
        ).setTypeFilter(TypeFilter.CITIES).setCountry("AU").build(this)

        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) {
            return
        }
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get Place data from intent
                val place = Autocomplete.getPlaceFromIntent(data)
                binding.etSuburb.setText(place.name)
                KeyboardUtils.hideKeyboard(this, snackBar)
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data)
                //Log.i("place_address_status", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Toast.makeText(this, "Result got cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun createAuthUserWithEmailAndPassword() {
        try {
            viewModel.MLDFirebaseSignup.value!!.email = RentalHomesApp.appSessionManager.getString(
                this,
                AppSessionManager.PREF_NAME,
                AppSessionManager.PREF_KEY_EMAIL
            )
            viewModel.MLDFirebaseSignup.value!!.password = RentalHomesApp.appSessionManager.getString(
                this,
                AppSessionManager.PREF_NAME,
                AppSessionManager.PREF_KEY_PASSWORD
            )
            val signUpRequest: LoginRequest = viewModel.MLDFirebaseSignup.value!!
            viewModel.mAuth.createUserWithEmailAndPassword(
                signUpRequest.email!!.trim(),
                signUpRequest.password!!.trim()
            )
                .addOnCompleteListener(this) { task ->
                    viewModel.doTaskOnCreateUserWithEmailAndPasswordCompletion(
                        task
                    )
                }
        } catch (e: Exception) {
            Log.e(TAG, "error: ${e.localizedMessage}")
            e.printStackTrace()
        }
    }

    override fun signInWithFirebase() {
        try {
            Log.e(TAG, "aaa===>   Start")
            Log.e(TAG, "Email===>   ${binding.etEmail.text.toString()}")
            Log.e(TAG, "PAssword===>   ${binding.etPassword.text.toString()}")
            buyerSignUpVM.mAuth.signInWithEmailAndPassword(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            ).addOnCompleteListener(
                this
            ) { task -> buyerSignUpVM.doLoginAuthUserWithEmailAndPassword(task) }
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "aaa===>" + e.printStackTrace() + " " + e.localizedMessage)
            e.printStackTrace()
        }
    }

    override fun onGetOTPClick() {
        if (isValidEmail()) {
//            buyerSignUpVM.getOTPAPICall(binding.etEmail.text.toString(), 2)
            onGetOTPComplete()
        }
    }

    override fun onGetOTPComplete() {
        binding.clOTP.visibility = View.VISIBLE
        verifiedEmail = binding.etEmail.text.toString().trim()
        isVerified = false
        binding.btnContinue.text = getString(R.string.verify_otp)
        GlobalMethods.disableButton(this@BuyerSignUpActivity, binding.btnContinue)
    }

    override fun onOtpVerificationComplete() {
        isVerified = true
        binding.clOTP.visibility = View.GONE
        binding.edtOtp1.text!!.clear()
        binding.edtOtp2.text!!.clear()
        binding.edtOtp3.text!!.clear()
        binding.edtOtp4.text!!.clear()
        nextToPassword()
        currentView = 4
        disableOPTButton()
    }

    private fun generateToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            it?.let {
                Log.e(TAG, "firebase token: " + it)
                RentalHomesApp.appSessionManager.setDeviceToken(this, it)
            }

        }
    }

    //Set visibility of password on click of eye icon
    private fun setPasswordVisibility() {
        if (!isPassVisible) {
            isPassVisible = true
            binding.ivVisIconPass.setImageResource(R.drawable.ic_visibility_on)  //Set visibility off icon
            binding.etPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance() //Hide the text of password
        } else {
            isPassVisible = false
            binding.ivVisIconPass.setImageResource(R.drawable.ic_visibility_off) //Set visibility on icon
            binding.etPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()    //Visible the text of password
        }
        binding.etPassword.setSelection(binding.etPassword.text.toString().length)  //Set cursor at last
    }

    //Set visibility of password on click of eye icon
    private fun setConfPassVisibility() {
        if (!isConfPassVisible) {
            isConfPassVisible = true
            binding.ivVisIconConf.setImageResource(R.drawable.ic_visibility_on)  //Set visibility off icon
            binding.etConfirmPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance() //Hide the text of password
        } else {
            isConfPassVisible = false
            binding.ivVisIconConf.setImageResource(R.drawable.ic_visibility_off) //Set visibility on icon
            binding.etConfirmPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()    //Visible the text of password
        }
        binding.etConfirmPassword.setSelection(binding.etConfirmPassword.text.toString().length)  //Set cursor at last
    }

    private fun disableOPTButton() {
        binding.tvGetOTP.isEnabled = false
        binding.tvGetOTP.setTextColor(getColor(R.color.grey_100))
    }

    private fun enableOTPButton() {
        binding.tvGetOTP.isEnabled = true
        binding.tvGetOTP.setTextColor(getColor(R.color.black))
    }
}