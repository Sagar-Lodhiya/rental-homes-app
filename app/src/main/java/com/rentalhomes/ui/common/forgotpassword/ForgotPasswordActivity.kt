package com.rentalhomes.ui.common.forgotpassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.databinding.ActivityForgotPasswordBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods
import com.rentalhomes.utils.KeyboardUtils

class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding, ForgotPasswordVM>(),
    View.OnClickListener, ForgotPasswordNavigator {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var snackBar: View
    private lateinit var forgotPasswordVM: ForgotPasswordVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setOnListener()
    }

    override fun setHeader() {
        TODO("Not yet implemented")
    }

    override val bindingVariable: Int
        get() = BR.forgotPasswordVM

    override val layoutId: Int
        get() = R.layout.activity_forgot_password

    override val viewModel: ForgotPasswordVM
        get() {
            forgotPasswordVM = ViewModelProvider(this).get(ForgotPasswordVM::class.java)
            return forgotPasswordVM
        }

    override fun setObservers() {
        TODO("Not yet implemented")
    }

    override fun onResponse() {
        binding.etEmail.text = null
        binding.etEmail.clearFocus()

        if (isValidEmail()) {
            GlobalMethods.enableButton(this@ForgotPasswordActivity, binding.btnSubmit)
        } else {
            GlobalMethods.disableButton(this@ForgotPasswordActivity, binding.btnSubmit)
        }
    }

    private fun init() {
        //Initialization is done here
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)
        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this@ForgotPasswordActivity
            it.forgotPasswordVM = forgotPasswordVM
        }
        forgotPasswordVM.attachContext(this)
        forgotPasswordVM.setNavigator(this)

        //Initialize snackBar object here
        snackBar = findViewById(android.R.id.content)
    }

    //setOnListener define here
    private fun setOnListener() {
        binding.ivBack.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)

        //Email TextWatcher to enable Submit button
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
                    GlobalMethods.enableButton(this@ForgotPasswordActivity, binding.btnSubmit)
                } else {
                    GlobalMethods.disableButton(this@ForgotPasswordActivity, binding.btnSubmit)
                }
            }

        })
    }

    //All the clicks implemented here
    override fun onClick(v: View?) {
        v?.let { view ->
            when (view.id) {
                R.id.ivBack -> {
                    onBackPressed()
                }
                R.id.btnSubmit -> {
                    if (isValidEmail()) {
                        Toast.makeText(this,"New Password Link has been sent to your mail",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun showProgress() {
        showProgressDialog()
    }

    override fun hideProgress() {
        hideProgressDialog()
    }

    override fun setMessageComingFromServer(it: String) {
        KeyboardUtils.hideKeyboard(this,snackBar)
        AlertDialogUtils.showSnakeBar(it, snackBar, this)
    }

    //Email validation
    private fun isValidEmail(): Boolean {
        val email: String = binding.etEmail.text.toString().trim()

        if (!GlobalMethods.isValidEmail(email)) {
            binding.etEmail.requestFocus()
            binding.tvValEmail.text = resources.getString(R.string.msg_invalid_email)
            return false
        }
        return true
    }

    override fun isValid(): Boolean {
        val email: String = binding.etEmail.text.toString().trim()

        if (email.isEmpty()) {
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

        return true
    }
}