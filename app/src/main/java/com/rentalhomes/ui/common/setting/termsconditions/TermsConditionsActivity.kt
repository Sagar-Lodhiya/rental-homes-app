package com.rentalhomes.ui.common.setting.termsconditions

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.databinding.ActivityTermsConditionsBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods


class TermsConditionsActivity : BaseActivity<ActivityTermsConditionsBinding, TermsConditionsVM>(),
    TermsConditionsNavigator {
    private lateinit var binding: ActivityTermsConditionsBinding
    private lateinit var termsConditionsVM: TermsConditionsVM
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
            it.termsConditionsVM = termsConditionsVM
        }
        termsConditionsVM.attachContext(this)
        termsConditionsVM.setNavigator(this)

        //Initialize snackBar object here
        snackBar = findViewById(android.R.id.content)

        //JavaScript Enable
        binding.wvTermsConditions.settings.javaScriptEnabled = true

        binding.wvTermsConditions.loadUrl("https://generator.lorem-ipsum.info/terms-and-conditions")

    }

    //Data binding
    override val bindingVariable: Int
        get() = BR.termsConditionsVM
    override val layoutId: Int
        get() = R.layout.activity_terms_conditions
    override val viewModel: TermsConditionsVM
        get() {
            termsConditionsVM = ViewModelProvider(this).get(TermsConditionsVM::class.java)
            return termsConditionsVM
        }

    override fun setHeader() {
        binding.headerBar.tvTitle.text = resources.getString(R.string.terms_and_conditions_setting)
    }

    private fun setOnListener() {
        binding.headerBar.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun setObservers() {
        termsConditionsVM.mldTermsCondition.observe(this,{response->

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