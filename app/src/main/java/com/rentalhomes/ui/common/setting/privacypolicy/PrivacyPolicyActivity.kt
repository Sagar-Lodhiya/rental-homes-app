package com.rentalhomes.ui.common.setting.privacypolicy

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.databinding.ActivityPrivacyPolicyBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods

/**
 * Created by Dharmesh
 * */
class PrivacyPolicyActivity : BaseActivity<ActivityPrivacyPolicyBinding, PrivacyPolicyVM>(),
    PrivacyPolicyNavigator {
    private lateinit var binding: ActivityPrivacyPolicyBinding
    private lateinit var privacyPolicyVM: PrivacyPolicyVM
    private lateinit var snackBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setHeader()
        setOnListener()
        setObservers()
    }

    //Initialization is done here
    private fun init() {
        //Set status bar color
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)

        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this
            it.privacyPolicyVM = privacyPolicyVM
        }
        privacyPolicyVM.attachContext(this)
        privacyPolicyVM.setNavigator(this)

        //Initialize snackBar object here
        snackBar = findViewById(android.R.id.content)

        //JavaScript Enable
        binding.wvPrivacyPolicy.settings.javaScriptEnabled = true
        binding.wvPrivacyPolicy.loadUrl("https://www.lipsum.com/privacy")

    }

    //Data binding
    override val bindingVariable: Int
        get() = BR.privacyPolicyVM
    override val layoutId: Int
        get() = R.layout.activity_privacy_policy
    override val viewModel: PrivacyPolicyVM
        get() {
            privacyPolicyVM = ViewModelProvider(this).get(PrivacyPolicyVM::class.java)
            return privacyPolicyVM
        }

    override fun setHeader() {
        binding.headerBar.tvTitle.text = resources.getString(R.string.privacy_policy)
    }

    private fun setOnListener() {
        binding.headerBar.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun setObservers() {
        privacyPolicyVM.mldPrivacyPolicy.observe(this,{response->

        })
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
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
}