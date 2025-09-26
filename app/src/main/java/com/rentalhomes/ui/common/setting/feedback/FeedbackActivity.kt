package com.rentalhomes.ui.common.setting.feedback

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.databinding.ActivityFeedbackBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods
import com.rentalhomes.utils.KeyboardUtils

/**
 * Created by Dharmesh
 * */
class FeedbackActivity : BaseActivity<ActivityFeedbackBinding, FeedbackVM>(), FeedbackNavigator {
    private lateinit var binding: ActivityFeedbackBinding
    private lateinit var feedbackVM: FeedbackVM
    private lateinit var snackBar: View

    companion object {
        private val TAG = FeedbackActivity::class.simpleName
    }

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
            it.feedbackVM = feedbackVM
        }
        feedbackVM.attachContext(this)
        feedbackVM.setNavigator(this)

        //Initialize snackBar object here
        snackBar = findViewById(android.R.id.content)

    }

    //Data binding
    override val bindingVariable: Int
        get() = BR.feedbackVM
    override val layoutId: Int
        get() = R.layout.activity_feedback
    override val viewModel: FeedbackVM
        get() {
            feedbackVM = ViewModelProvider(this).get(FeedbackVM::class.java)
            return feedbackVM
        }

    private fun setOnListener() {
        binding.headerBar.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.etShareExperience.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //To enable and disable Submit Feedback button
                if (isValid() && p0.toString().isNotEmpty()) {
                    GlobalMethods.enableButton(this@FeedbackActivity, binding.btnSubmitFeedback)
                } else {
                    GlobalMethods.disableButton(this@FeedbackActivity, binding.btnSubmitFeedback)
                }

                //Check and show validation of Share your experience
                if (!isValid() && p0.toString().isNotEmpty()) {
                    binding.tvValShareExp.visibility = View.VISIBLE
                } else {
                    binding.tvValShareExp.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.btnSubmitFeedback.setOnClickListener {
            if (isValid()) {
                Toast.makeText(this,"Feedback Sent Successfully to the admin",Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun isValid(): Boolean {
        val shareExperience = binding.etShareExperience.text.toString().trim()

        if (shareExperience.length < 4) {
            binding.tvValShareExp.text = resources.getString(R.string.msg_length_share_experience)
            return false
        }

        return true
    }

    override fun setHeader() {
        binding.headerBar.tvTitle.text = resources.getString(R.string.feedback)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }

    override fun setObservers() {
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

    override fun onResponse() {
        binding.etShareExperience.text = null
        binding.etShareExperience.clearFocus()

        if (isValid()) {
            GlobalMethods.enableButton(this@FeedbackActivity, binding.btnSubmitFeedback)
        } else {
            GlobalMethods.disableButton(this@FeedbackActivity, binding.btnSubmitFeedback)
        }

        Handler().postDelayed({
            onBackPressed()
        }, 2000)
    }
}