package com.rentalhomes.ui.common.signin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.rentalhomes.BR
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.R
import com.rentalhomes.databinding.ActivitySignInBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.ui.agent.homescreen.AgentHomeActivity
import com.rentalhomes.ui.buyer.home.BuyerHomeActivity
import com.rentalhomes.ui.common.forgotpassword.ForgotPasswordActivity
import com.rentalhomes.ui.common.signup.UserSelectionActivity
import com.rentalhomes.utils.*
import java.lang.Exception

class SignInActivity : BaseActivity<ActivitySignInBinding, SignInVM>(), View.OnClickListener,
    SignInNavigator {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var snackBar: View
    private lateinit var signInVM: SignInVM


    //Animation transition objects
    private var slideLeft: Animation? = null
    private var slideRight: Animation? = null

    private var isPasswordVisible: Boolean = false //to check password visibility
    private var isSignInWithEmail: Boolean = true //to check is sign in with email or mobile

    companion object {
        private val TAG = SignInActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        generateToken()
        init()
        setOnListener()

        //Code for test crash
        /*val crashButton = Button(this)
        crashButton.text = "Test Crash"
        crashButton.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }

        addContentView(crashButton, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT))*/
    }

    private fun generateToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            it?.let {
                RentalHomesApp.appSessionManager.setDeviceToken(this, it)
                Log.e(TAG, "generateToken: " + it)
            }

        }
    }

    private fun init() {
        //Initialization is done here
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)
        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this@SignInActivity
            it.signInVM = signInVM
        }
        signInVM.attachContext(this)
        signInVM.setNavigator(this)

        //Initialize animation here
        slideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left)
        slideRight = AnimationUtils.loadAnimation(this, R.anim.slide_right)

        //Initialize snackBar object here
        snackBar = findViewById(android.R.id.content)

    }

    override fun setHeader() {
    }

    private fun firebaseSignInCall() {
        try {
            Log.e("SIGN_IN", "aaa===>   Start")
//            signInVM.mldFirebaseSignIn.value!!.email =  binding.etEmail.text.toString()
//            signInVM.mldFirebaseSignIn.value!!.password =  binding.etPassword.text.toString()
            signInVM.mAuth.signInWithEmailAndPassword(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            ).addOnCompleteListener(
                this
            ) { task -> signInVM.doLoginAuthUserWithEmailAndPassword(task) }
        } catch (e: Exception) {
            Log.e("SIGN_IN", "aaa===>" + e.printStackTrace() + " " + e.localizedMessage)
            e.printStackTrace()
        }
    }


    override val bindingVariable: Int
        get() = BR.signInVM

    override val layoutId: Int
        get() = R.layout.activity_sign_in

    override val viewModel: SignInVM
        get() {
            signInVM = ViewModelProvider(this).get(SignInVM::class.java)
            return signInVM
        }

    override fun setObservers() {
    }

    //setOnListener define here
    private fun setOnListener() {
        binding.ivVisibilityIcon.setOnClickListener(this)
        binding.btnSignIn.setOnClickListener(this)
        binding.tvForgotPass.setOnClickListener(this)
        binding.tvSignUp.setOnClickListener(this)


        //Email TextWatcher to enable sign in button
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
                if (isValidEmail() && isValidPassword()) {
                    GlobalMethods.enableButton(this@SignInActivity, binding.btnSignIn)
                } else {
                    GlobalMethods.disableButton(this@SignInActivity, binding.btnSignIn)
                }
            }
        })

        //Password TextWatcher to enable sign in button
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
                if (isValidEmail() && isValidPassword()) {
                    GlobalMethods.enableButton(this@SignInActivity, binding.btnSignIn)
                } else {
                    GlobalMethods.disableButton(this@SignInActivity, binding.btnSignIn)
                }
            }
        })

    }

    fun isValidEmail(): Boolean {
        val email: String = binding.etEmail.text.toString().trim()
        /*if (TextUtils.isEmpty(email)) {
            binding.etEmail.requestFocus()
            binding.tvValEmail.text = resources.getString(R.string.msg_empty_email)
            return false
        }*/

        if (!GlobalMethods.isValidEmail(email)) {
//            binding.etEmail.requestFocus()
            binding.tvValEmail.text = resources.getString(R.string.msg_invalid_email)
            return false
        }
        return true
    }

    fun isValidPassword(): Boolean {
        val password: String = binding.etPassword.text.toString().trim()

        if (password.isEmpty()) {
            binding.tvValPassword.text = resources.getString(R.string.msg_empty_password)
            return false
        }
        /*if (!GlobalMethods.isValidPassword(password)) {
            binding.tvValPassword.text = resources.getString(R.string.msg_invalid_password)
            return false
        }*/

        if (GlobalMethods.isContainEmoji(password)) {
            binding.tvValPassword.text =
                resources.getString(R.string.msg_password_not_contain_emoji)
            return false
        }
        return true
    }

    //All the clicks implemented here
    override fun onClick(v: View?) {
        v?.let { view ->
            when (view.id) {
                R.id.ivVisibilityIcon -> {
                    setPasswordVisibility()
                }
                R.id.btnSignIn -> {
                    if (isValidEmail()) {
                        KeyboardUtils.hideKeyboard(this, snackBar)
//                        firebaseSignInCall()
//                        signInVM.loginCall()
                        if(binding.etEmail.editableText.toString() =="buyer@gmail.com" && binding.etPassword.editableText.toString() =="Test@123") {
                            startActivity(Intent(this, BuyerHomeActivity::class.java))
                        }else if(binding.etEmail.editableText.toString() =="agent@gmail.com" && binding.etPassword.editableText.toString() =="Test@123"){
                            startActivity(Intent(this, AgentHomeActivity::class.java))
                        }else{
                            Toast.makeText(this,"Enter Proper Credentials", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                R.id.tvForgotPass -> {
                    KeyboardUtils.hideKeyboard(this, snackBar)
                    startActivity(Intent(this, ForgotPasswordActivity::class.java))
                }
                R.id.tvSignUp -> {
                    KeyboardUtils.hideKeyboard(this, snackBar)
                    startActivity(Intent(this, UserSelectionActivity::class.java))
                }
            }
        }
    }

    //Set visibility of password on click of eye icon
    private fun setPasswordVisibility() {
        if (!isPasswordVisible) {
            isPasswordVisible = true
            binding.ivVisibilityIcon.setImageResource(R.drawable.ic_visibility_on)  //Set visibility off icon
            binding.etPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance() //Hide the text of password
        } else {
            isPasswordVisible = false
            binding.ivVisibilityIcon.setImageResource(R.drawable.ic_visibility_off) //Set visibility on icon
            binding.etPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()    //Visible the text of password
        }
        binding.etPassword.setSelection(binding.etPassword.text.toString().length)  //Set cursor at last
    }

    override fun showProgress() {
        CommonUtils.showProgressDialog(this@SignInActivity)
    }

    override fun hideProgress() {
        CommonUtils.hideProgressDialog()
    }

    //Check validation of email, password, phone
    override fun isValid(): Boolean {
        val email: String = binding.etEmail.text.toString().trim()
        val password: String = binding.etPassword.text.toString().trim()

        //Email & Password validations
        if (TextUtils.isEmpty(email)) {
            binding.etEmail.requestFocus()
            KeyboardUtils.hideKeyboard(this, snackBar)
            AlertDialogUtils.showSnakeBar(
                resources.getString(R.string.msg_empty_email),
                snackBar,
                this
            )
            return false
        }

        if (!GlobalMethods.isValidEmail(email)) {
            binding.etEmail.requestFocus()
            KeyboardUtils.hideKeyboard(this, snackBar)
            AlertDialogUtils.showSnakeBar(
                resources.getString(R.string.msg_invalid_email),
                snackBar,
                this
            )
            return false
        }

        if (TextUtils.isEmpty(password)) {
            binding.etPassword.requestFocus()
            KeyboardUtils.hideKeyboard(this, snackBar)
            AlertDialogUtils.showSnakeBar(
                resources.getString(R.string.msg_empty_password),
                snackBar,
                this
            )
            return false
        }

        if (password.length < 6) {
            binding.etPassword.requestFocus()
            KeyboardUtils.hideKeyboard(this, snackBar)
            AlertDialogUtils.showSnakeBar(
                resources.getString(R.string.msg_password_length),
                snackBar,
                this
            )
            return false
        }

        if (!GlobalMethods.isValidPassword(password)) {
            binding.etPassword.requestFocus()
            KeyboardUtils.hideKeyboard(this, snackBar)
            AlertDialogUtils.showAlert(this, resources.getString(R.string.msg_invalid_password))
            return false
        }

        if (GlobalMethods.isContainEmoji(password)) {
            binding.etPassword.requestFocus()
            KeyboardUtils.hideKeyboard(this, snackBar)
            AlertDialogUtils.showSnakeBar(
                resources.getString(R.string.msg_password_not_contain_emoji),
                snackBar,
                this
            )
            return false
        }
        return true
    }

    override fun goToAgent() {
        intent = Intent(this, AgentHomeActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun goToBuyer() {
        intent = Intent(this, BuyerHomeActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun setMessageComingFromServer(it: String) {
        AlertDialogUtils.showSnakeBar(it, snackBar, this)
    }

    override fun onSignInCompleted() {
        firebaseSignInCall()
    }
}