package com.rentalhomes.ui.common.setting

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.databinding.ActivitySettingBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.ui.common.setting.aboutapp.AboutAppActivity
import com.rentalhomes.ui.common.setting.affiliations.AffiliationsActivity
import com.rentalhomes.ui.common.setting.changepassword.ChangePasswordActivity
import com.rentalhomes.ui.common.setting.contactus.ContactUsActivity
import com.rentalhomes.ui.common.setting.feedback.FeedbackActivity
import com.rentalhomes.ui.common.setting.privacypolicy.PrivacyPolicyActivity
import com.rentalhomes.ui.common.setting.termsconditions.TermsConditionsActivity
import com.rentalhomes.ui.common.signin.SignInActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods
import com.rentalhomes.utils.Utils.shareApp
import java.util.*

/**
 * Created by Dharmesh
 * */
class SettingActivity : BaseActivity<ActivitySettingBinding, SettingVM>(), SettingNavigator {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var settingVM: SettingVM
    private lateinit var snackBar: View

    private val settingList = ArrayList<SettingModel>()
    private lateinit var settingAdapter: SettingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setHeader()
        setOnListener()
        setSettingModel()
        setSettingRecyclerView()
    }

    private fun setOnListener() {
        binding.headerBar.ivBack.setOnClickListener { finish() }
    }

    //Initialization is done here
    private fun init() {
        //Set status bar color
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)

        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this
            it.settingVM = settingVM
        }
        settingVM.attachContext(this)
        settingVM.setNavigator(this)

        //Initialize snackBar object here
        snackBar = findViewById(android.R.id.content)
    }

    //Data binding
    override val bindingVariable: Int
        get() = BR.settingVM
    override val layoutId: Int
        get() = R.layout.activity_setting
    override val viewModel: SettingVM
        get() {
            settingVM = ViewModelProvider(this).get(SettingVM::class.java)
            return settingVM
        }

    private fun setSettingRecyclerView() {
        settingAdapter = SettingAdapter(this, settingList, this)
        binding.rvSettings.apply {
            this.adapter = settingAdapter
        }
    }

    override fun onSettingItemClick(position: Int) {
        when (position) {
            0 -> {
                startActivity(Intent(this, TermsConditionsActivity::class.java))
            }
            1 -> {
                startActivity(Intent(this, PrivacyPolicyActivity::class.java))
            }
            2 -> {
                startActivity(Intent(this, FeedbackActivity::class.java))
            }
            3 -> {
                startActivity(Intent(this, ContactUsActivity::class.java))
            }
            4 -> {
                startActivity(Intent(this, AffiliationsActivity::class.java))
            }
            5 -> {
                startActivity(Intent(this, AboutAppActivity::class.java))
            }
            6 -> {
                startActivity(Intent(this, ChangePasswordActivity::class.java))
            }
            7 -> {
                shareApp(this)
            }
            8 -> {
                showLogoutDialog()
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
        AlertDialogUtils.showSnakeBar(it, snackBar, this)
    }

    override fun onLogoutComplete() {
        clearDataAndLogout()
    }

    //Custom alert Logout
    private fun showLogoutDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_dialog_logout, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()

        //Cancel button click
        dialogView.findViewById<AppCompatButton>(R.id.btnCancel).setOnClickListener {
            alertDialog.dismiss()
        }
        //Logout button click
        dialogView.findViewById<AppCompatButton>(R.id.btnLogout).setOnClickListener {
            alertDialog.dismiss()
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    private fun setSettingModel() {
        settingList.clear()
        settingList.add(
            SettingModel(
                getString(R.string.terms_and_conditions_setting),
                R.drawable.ic_terms_conditions
            )
        )
        settingList.add(
            SettingModel(
                getString(R.string.privacy_policy),
                R.drawable.ic_terms_conditions
            )
        )
        settingList.add(
            SettingModel(
                getString(R.string.feedback),
                R.drawable.ic_feedback
            )
        )
        settingList.add(
            SettingModel(
                getString(R.string.contact_us),
                R.drawable.ic_contact_us
            )
        )
        settingList.add(
            SettingModel(
                getString(R.string.affiliations),
                R.drawable.ic_affiliations
            )
        )
        settingList.add(
            SettingModel(
                getString(R.string.about) + " " + getString(R.string.app_name),
                R.drawable.ic_about_app
            )
        )
        settingList.add(
            SettingModel(
                getString(R.string.change_email_and_password),
                R.drawable.ic_change_email_password
            )
        )
        settingList.add(
            SettingModel(
                getString(R.string.share) + " " + getString(R.string.app_name),
                R.drawable.ic_share_app
            )
        )
        settingList.add(
            SettingModel(
                getString(R.string.logout),
                R.drawable.ic_logout
            )
        )
    }

    override fun setHeader() {
        binding.headerBar.tvTitle.text = resources.getString(R.string.settings)
    }

    override fun setObservers() {
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }
}