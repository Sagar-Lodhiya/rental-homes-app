package com.rentalhomes.ui.agent.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.databinding.ActivityProfileBinding
import com.rentalhomes.ui.agent.editprofile.EditProfileActivity
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.ui.common.setting.SettingActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods
import com.rentalhomes.utils.KeyboardUtils


class ProfileActivity : BaseActivity<ActivityProfileBinding, ProfileVM>(), ProfileNavigator,
    View.OnClickListener {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var snackBar: View
    private lateinit var profileVM: ProfileVM

    private var isOtherUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setHeader()
        setOnListener()
        Glide.with(this)
            .load("https://randomuser.me/api/portraits/men/75.jpg")
            .placeholder(R.drawable.ic_profile_place_holder)
            .error(R.drawable.ic_profile_place_holder)
            .into(binding.civProfile)
    }

    private fun init() {
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)
        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this
            it.profileVM = profileVM
        }
        profileVM.attachContext(this)
        profileVM.setNavigator(this)
        snackBar = findViewById(android.R.id.content)
    }

    override val bindingVariable: Int
        get() = BR.profileVM
    override val layoutId: Int
        get() = R.layout.activity_profile
    override val viewModel: ProfileVM
        get() {
            profileVM = ViewModelProvider(this).get(ProfileVM::class.java)
            return profileVM
        }


    override fun setHeader() {
        binding.headerBar.tvTitle.text = getString(R.string.profile)
        binding.headerBar.ivSetting.visibility = View.VISIBLE
    }

    private fun setOnListener() {
        binding.headerBar.ivBack.setOnClickListener(this)
        binding.headerBar.ivSetting.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        v?.let { view ->
            when (view.id) {
                R.id.ivBack -> {
                    KeyboardUtils.hideKeyboard(this, snackBar)
                    onBackPressed()
                }
                R.id.ivSetting -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                }
            }
        }
    }



    override fun onEditProfileClicked() {
        startActivity(Intent(this, EditProfileActivity::class.java))
    }

    override fun setObservers() {
        profileVM.mldProfileDetails.observe(this) { profileData ->

            val thumbnailRequest = Glide.with(this).load(profileData.profilePic)



            binding.tvUserName.text = "${profileData.firstName} ${profileData.lastName}"
            binding.tvAgencyName.text = profileData.agencyName
            binding.tvEmail.text = profileData.email
            binding.tvPhoneNumber.text = "+61 ${profileData.mobile.toString()}"

            binding.vdPhoneBG.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + profileData.mobile.toString())
                startActivity(intent)
            }

            binding.tvPhoneNumber.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + profileData.mobile.toString())
                startActivity(intent)
            }

            binding.mainView.visibility = View.VISIBLE
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
}