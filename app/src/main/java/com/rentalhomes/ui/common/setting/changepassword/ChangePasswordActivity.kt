package com.rentalhomes.ui.common.setting.changepassword

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.rentalhomes.BR
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.R
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.databinding.ActivityChangePasswordBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods
import com.rentalhomes.utils.KeyboardUtils


/**
 * Created by Dharmesh
 * */
class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding, ChangePasswordVM>(),
    ChangePasswordNavigator {
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var changePasswordVM: ChangePasswordVM
    private lateinit var snackBar: View

    //initialize Typeface
    private lateinit var regularTypeface: Typeface
    private lateinit var boldTypeface: Typeface

    private var isEmailPassVisible: Boolean = false //to check password visibility
    private var isPassCurrentVisible: Boolean = false //to check password visibility
    private var isPassNewVisible: Boolean = false //to check password visibility
    private var isPassConfirmVisible: Boolean = false //to check password visibility

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setHeader()
        setOnListener()
    }

    //Initialization is done here
    private fun init() {
        //Set status bar color
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)

        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this
            it.changePasswordVM = changePasswordVM
        }
        changePasswordVM.attachContext(this)
        changePasswordVM.setNavigator(this)

        //Initialize snackBar object here
        snackBar = findViewById(android.R.id.content)

        regularTypeface =
            Typeface.createFromAsset(this.assets, "fonts/product_sans_regular.ttf")
        boldTypeface =
            Typeface.createFromAsset(this.assets, "fonts/product_sans_bold.ttf")
    }

    //Data binding
    override val bindingVariable: Int
        get() = BR.changePasswordVM
    override val layoutId: Int
        get() = R.layout.activity_change_password
    override val viewModel: ChangePasswordVM
        get() {
            changePasswordVM = ViewModelProvider(this).get(ChangePasswordVM::class.java)
            return changePasswordVM
        }

    override fun setHeader() {
        binding.headerBar.tvTitle.text = resources.getString(R.string.change_email_and_password)
    }

    private fun setOnListener() {
        binding.headerBar.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.ivVisEmailCurrent.setOnClickListener {
            setEmailPasswordVisibility()
        }
        binding.ivVisPassCurrent.setOnClickListener {
            setPassCurrentVisibility()
        }
        binding.ivVisPassNew.setOnClickListener {
            setPassNewVisibility()
        }
        binding.ivVisPassConfirm.setOnClickListener {
            setPassConfirmVisibility()
        }

        //Email TextWatcher
        binding.etCurrentEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //To enable and disable Submit Feedback button
                if (isValidCurrentEmail() && isValidNewEmail() && isValidConfirmEmail() && binding.etEmailPassword.text!!.isNotEmpty() && p0.toString()
                        .isNotEmpty()
                ) {
                    GlobalMethods.enableButton(
                        this@ChangePasswordActivity,
                        binding.btnChangeEmail
                    )
                } else {
                    GlobalMethods.disableButton(
                        this@ChangePasswordActivity,
                        binding.btnChangeEmail
                    )
                }

                //Check and show validation of Current Password
                if (!isValidCurrentEmail() && p0.toString().isNotEmpty()) {
                    binding.tvValCurrentEmail.visibility = View.VISIBLE
                } else {
                    binding.tvValCurrentEmail.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        /*binding.etEmailPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //To enable and disable Submit Feedback button
                if (isValidCurrentEmail() && isValidNewEmail() && isValidConfirmEmail() && binding.etEmailPassword.text!!.isNotEmpty() && p0.toString()
                        .isNotEmpty()
                ) {
                    GlobalMethods.enableButton(
                        this@ChangePasswordActivity,
                        binding.btnChangeEmail
                    )
                } else {
                    GlobalMethods.disableButton(
                        this@ChangePasswordActivity,
                        binding.btnChangeEmail
                    )
                }

                //Check and show validation of Current Password
                if (p0.toString().isEmpty()) {
                    binding.tvValEmailPassword.visibility = View.VISIBLE
                } else {
                    binding.tvValEmailPassword.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })*/

        binding.etNewEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //To enable and disable Submit Feedback button
                if (isValidCurrentEmail() && isValidNewEmail() && isValidConfirmEmail() && binding.etEmailPassword.text!!.isNotEmpty() && p0.toString()
                        .isNotEmpty()
                ) {
                    GlobalMethods.enableButton(
                        this@ChangePasswordActivity,
                        binding.btnChangeEmail
                    )
                } else {
                    GlobalMethods.disableButton(
                        this@ChangePasswordActivity,
                        binding.btnChangeEmail
                    )
                }

                //Check and show validation of New Password
                if (!isValidNewEmail() && p0.toString().isNotEmpty()) {
                    binding.tvValNewEmail.visibility = View.VISIBLE
                } else {
                    binding.tvValNewEmail.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.etConfirmEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //To enable and disable Submit Feedback button
                if (isValidCurrentEmail() && isValidNewEmail() && isValidConfirmEmail() && binding.etEmailPassword.text!!.isNotEmpty() && p0.toString()
                        .isNotEmpty()
                ) {
                    GlobalMethods.enableButton(
                        this@ChangePasswordActivity,
                        binding.btnChangeEmail
                    )
                } else {
                    GlobalMethods.disableButton(
                        this@ChangePasswordActivity,
                        binding.btnChangeEmail
                    )
                }

                //Check and show validation of Confirm Password
                if (!isValidConfirmEmail() && p0.toString().isNotEmpty()) {
                    binding.tvValConfirmEmail.visibility = View.VISIBLE
                } else {
                    binding.tvValConfirmEmail.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        //Password TextWatcher
        binding.etCurrentPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //To enable and disable Submit Feedback button
//                if (/*isValidCurrentPass() &&*/ isValidNewPass() && isValidConfirmPass() && p0.toString()
//                        .isNotEmpty()
//                ) {
//                    GlobalMethods.enableButton(
//                        this@ChangePasswordActivity,
//                        binding.btnChangePassword
//                    )
//                } else {
//                    GlobalMethods.disableButton(
//                        this@ChangePasswordActivity,
//                        binding.btnChangePassword
//                    )
//                }

                /*//Check and show validation of Current Password
                if (*//*!isValidCurrentPass() &&*//* p0.toString().isNotEmpty()) {
                    binding.tvValCurrentPassword.visibility = View.VISIBLE
                } else {
                    binding.tvValCurrentPassword.visibility = View.GONE
                }*/
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.etNewPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //To enable and disable Submit Feedback button
                if (isValidNewPass() /*&& isValidCurrentPass()*/ && isValidConfirmPass() && p0.toString()
                        .isNotEmpty()
                ) {
                    GlobalMethods.enableButton(
                        this@ChangePasswordActivity,
                        binding.btnChangePassword
                    )
                } else {
                    GlobalMethods.disableButton(
                        this@ChangePasswordActivity,
                        binding.btnChangePassword
                    )
                }

                //Check and show validation of New Password
                if (!isValidNewPass() && p0.toString().isNotEmpty()) {
                    binding.tvValNewPassword.visibility = View.VISIBLE
                } else {
                    binding.tvValNewPassword.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.etConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //To enable and disable Submit Feedback button
                if (isValidConfirmPass() && isValidNewPass() && p0.toString()
                        .isNotEmpty()
                ) {
                    GlobalMethods.enableButton(
                        this@ChangePasswordActivity,
                        binding.btnChangePassword
                    )
                } else {
                    GlobalMethods.disableButton(
                        this@ChangePasswordActivity,
                        binding.btnChangePassword
                    )
                }

                //Check and show validation of Confirm Password
                if (!isValidConfirmPass() && p0.toString().isNotEmpty()) {
                    binding.tvValConfirmPassword.visibility = View.VISIBLE
                } else {
                    binding.tvValConfirmPassword.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    //Email validation methods
    private fun isValidCurrentEmail(): Boolean {
        val currentEmail = binding.etCurrentEmail.text.toString().trim()

        if (!GlobalMethods.isValidEmail(currentEmail)) {
            binding.tvValCurrentEmail.text = resources.getString(R.string.msg_invalid_email)
            return false
        }
        return true
    }

    private fun isValidNewEmail(): Boolean {
        val newEmail = binding.etNewEmail.text.toString().trim()

        if (!GlobalMethods.isValidEmail(newEmail)) {
            binding.tvValNewEmail.text = resources.getString(R.string.msg_invalid_email)
            return false
        }
        return true
    }

    private fun isValidConfirmEmail(): Boolean {
        val newEmail = binding.etNewEmail.text.toString().trim()
        val confirmEmail = binding.etConfirmEmail.text.toString().trim()

        if (newEmail != confirmEmail) {
            binding.tvValConfirmEmail.text =
                resources.getString(R.string.msg_new_email_confirm_email_match)
            return false
        }
        return true
    }


    private fun isValidNewPass(): Boolean {
        val newPass = binding.etNewPassword.text.toString().trim()

        if (newPass.length < 6) {
            binding.tvValNewPassword.text = resources.getString(R.string.msg_password_length)
            return false
        }

        if (GlobalMethods.isContainEmoji(newPass)) {
            binding.tvValNewPassword.text =
                resources.getString(R.string.msg_password_not_contain_emoji)
            return false
        }
        return true
    }

    private fun isValidConfirmPass(): Boolean {
        val newPass = binding.etNewPassword.text.toString().trim()
        val confirmPass = binding.etConfirmPassword.text.toString().trim()

        if (newPass != confirmPass) {
            binding.tvValConfirmPassword.text =
                resources.getString(R.string.msg_password_confirm_pass_not_match)
            return false
        }
        return true
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }

    override fun setObservers() {

    }

    override fun onClickChangeEmail() {
        if (isValidCurrentEmail() && isValidNewEmail() && isValidConfirmEmail() && binding.etEmailPassword.text!!.isNotEmpty()) {
            Toast.makeText(this,"Email Changed Successfully",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClickChangePassword() {
        if (isValidConfirmPass() && isValidNewPass() && binding.etCurrentPassword.text!!.isNotEmpty()) {
            Toast.makeText(this,"Password Changed Successfully",Toast.LENGTH_SHORT).show()
        }
    }

    override fun showProgress() {
        showProgressDialog()
    }

    override fun hideProgress() {
        hideProgressDialog()
    }

    override fun setMessageComingFromServer(it: String) {
        KeyboardUtils.hideKeyboard(this, snackBar)
        AlertDialogUtils.showSnakeBar(it, snackBar, this)
    }

    override fun onEmailResponse() {

        RentalHomesApp.appSessionManager.setString(
            this,
            AppSessionManager.PREF_NAME,
            AppSessionManager.PREF_KEY_EMAIL,
            binding.etNewEmail.text.toString()
        )

        binding.etEmailPassword.text = null
        binding.etCurrentEmail.text = null
        binding.etConfirmEmail.text = null
        binding.etNewEmail.text = null

        binding.etEmailPassword.clearFocus()
        binding.etCurrentEmail.clearFocus()
        binding.etConfirmEmail.clearFocus()
        binding.etNewEmail.clearFocus()

        if (isValidCurrentEmail() && isValidNewEmail() && isValidConfirmEmail() && binding.etCurrentPassword.text!!.isNotEmpty()) {
            GlobalMethods.enableButton(
                this@ChangePasswordActivity,
                binding.btnChangeEmail
            )
        } else {
            GlobalMethods.disableButton(
                this@ChangePasswordActivity,
                binding.btnChangeEmail
            )
        }

        Handler(Looper.getMainLooper()).postDelayed({
            onBackPressed()
        }, 2000)
    }

    override fun onPasswordResponse() {

        binding.etCurrentPassword.text = null
        binding.etConfirmPassword.text = null
        binding.etNewPassword.text = null


        binding.etCurrentPassword.clearFocus()
        binding.etConfirmPassword.clearFocus()
        binding.etNewPassword.clearFocus()

        if (isValidConfirmPass() && isValidNewPass() && binding.etCurrentPassword.text!!.isNotEmpty()) {
            GlobalMethods.enableButton(
                this@ChangePasswordActivity,
                binding.btnChangePassword
            )
        } else {
            GlobalMethods.disableButton(
                this@ChangePasswordActivity,
                binding.btnChangePassword
            )
        }

        Handler(Looper.getMainLooper()).postDelayed({
            onBackPressed()
        }, 2000)
    }

    override fun onEmailClick() {
        KeyboardUtils.hideKeyboard(this, snackBar)
        //Set font-family
        binding.tvEmail.typeface = boldTypeface
        binding.tvPassword.typeface = regularTypeface

        //Set visibility of view divider
        binding.vdEmail.visibility = View.VISIBLE
        binding.vdPassword.visibility = View.GONE

        //Set visibility of Email & Password layouts
        binding.clEmail.visibility = View.VISIBLE
        binding.clPassword.visibility = View.GONE
    }

    override fun onPasswordClick() {
        KeyboardUtils.hideKeyboard(this, snackBar)
        //Set font-family
        binding.tvPassword.typeface = boldTypeface
        binding.tvEmail.typeface = regularTypeface

        //Set visibility of view divider
        binding.vdPassword.visibility = View.VISIBLE
        binding.vdEmail.visibility = View.GONE

        //Set visibility of Email & Password layouts
        binding.clPassword.visibility = View.VISIBLE
        binding.clEmail.visibility = View.GONE
    }

    //Email current password visibility
    private fun setEmailPasswordVisibility() {
        if (!isEmailPassVisible) {
            isEmailPassVisible = true
            binding.ivVisEmailCurrent.setImageResource(R.drawable.ic_visibility_on)  //Set visibility off icon
            binding.etEmailPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance() //Hide the text of password
        } else {
            isEmailPassVisible = false
            binding.ivVisEmailCurrent.setImageResource(R.drawable.ic_visibility_off) //Set visibility on icon
            binding.etEmailPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()    //Visible the text of password
        }
        binding.etEmailPassword.setSelection(binding.etEmailPassword.text.toString().length)  //Set cursor at last
    }

    //Password current password visibility
    private fun setPassCurrentVisibility() {
        if (!isPassCurrentVisible) {
            isPassCurrentVisible = true
            binding.ivVisPassCurrent.setImageResource(R.drawable.ic_visibility_on)  //Set visibility off icon
            binding.etCurrentPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance() //Hide the text of password
        } else {
            isPassCurrentVisible = false
            binding.ivVisPassCurrent.setImageResource(R.drawable.ic_visibility_off) //Set visibility on icon
            binding.etCurrentPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()    //Visible the text of password
        }
        binding.etCurrentPassword.setSelection(binding.etCurrentPassword.text.toString().length)  //Set cursor at last
    }

    //Password current password visibility
    private fun setPassNewVisibility() {
        if (!isPassNewVisible) {
            isPassNewVisible = true
            binding.ivVisPassNew.setImageResource(R.drawable.ic_visibility_on)  //Set visibility off icon
            binding.etNewPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance() //Hide the text of password
        } else {
            isPassNewVisible = false
            binding.ivVisPassNew.setImageResource(R.drawable.ic_visibility_off) //Set visibility on icon
            binding.etNewPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()    //Visible the text of password
        }
        binding.etNewPassword.setSelection(binding.etNewPassword.text.toString().length)  //Set cursor at last
    }

    //Password current password visibility
    private fun setPassConfirmVisibility() {
        if (!isPassConfirmVisible) {
            isPassConfirmVisible = true
            binding.ivVisPassConfirm.setImageResource(R.drawable.ic_visibility_on)  //Set visibility off icon
            binding.etConfirmPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance() //Hide the text of password
        } else {
            isPassConfirmVisible = false
            binding.ivVisPassConfirm.setImageResource(R.drawable.ic_visibility_off) //Set visibility on icon
            binding.etConfirmPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()    //Visible the text of password
        }
        binding.etConfirmPassword.setSelection(binding.etConfirmPassword.text.toString().length)  //Set cursor at last
    }
}