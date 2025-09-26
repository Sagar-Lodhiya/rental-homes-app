package com.rentalhomes.ui.buyer.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.databinding.ActivityBuyerProfileBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.ui.buyer.editprofile.BuyerEditProfileActivity
import com.rentalhomes.ui.common.setting.SettingActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods

/**
 * Created by Malhar
 * */

class BuyerProfileActivity : BaseActivity<ActivityBuyerProfileBinding, BuyerProfileVM>(),
    BuyerProfileNavigator {
    private lateinit var binding: ActivityBuyerProfileBinding
    private lateinit var snackBar: View
    private lateinit var buyerProfileVM: BuyerProfileVM
    private lateinit var profilePic: String
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var mobile: String
    private lateinit var postCode: String
    private var isOtherUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setHeader()
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
            it.buyerProfileVM = buyerProfileVM
        }
        buyerProfileVM.attachContext(this)
        buyerProfileVM.setNavigator(this)
        snackBar = findViewById(android.R.id.content)
    }

    override val bindingVariable: Int
        get() = BR.buyerProfileVM
    override val layoutId: Int
        get() = R.layout.activity_buyer_profile
    override val viewModel: BuyerProfileVM
        get() {
            buyerProfileVM = ViewModelProvider(this).get(BuyerProfileVM::class.java)
            return buyerProfileVM
        }

    override fun setObservers() {

    }

    override fun setHeader() {
        binding.headerBar.tvTitle.text = getString(R.string.profile)
        binding.headerBar.ivSetting.visibility = View.VISIBLE
        binding.headerBar.ivSetting.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
        binding.headerBar.ivBack.setOnClickListener {
            onBackPressed()
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



    override fun onEditProfileClick() {
        val intent = Intent(this, BuyerEditProfileActivity::class.java)
        startActivity(intent)
    }


}