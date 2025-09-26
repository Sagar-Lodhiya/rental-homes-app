package com.rentalhomes.ui.common.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.rentalhomes.BR
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.R
import com.rentalhomes.data.network.Keys.AGENT
import com.rentalhomes.data.network.Keys.BUYER
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.databinding.ActivityUserSelectionBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.ui.common.signin.SignInActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalFields.USER_SELECTED
import com.rentalhomes.utils.GlobalMethods

class UserSelectionActivity : BaseActivity<ActivityUserSelectionBinding, UserSelectionVM>(),
    UserSelectionNavigator {
    private lateinit var binding: ActivityUserSelectionBinding
    private lateinit var snackBar: View
    private lateinit var userSelectionVM: UserSelectionVM
    private var userSelected: Int = 1 // userSelected (1) = Agent AND userSelected(2) = Buyer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)
        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this@UserSelectionActivity
            it.userSelectionVM = userSelectionVM
        }
        userSelectionVM.attachContext(this)
        userSelectionVM.setNavigator(this)
        snackBar = findViewById(android.R.id.content)
        binding.tvSignIn.setOnClickListener {
            intent = Intent(this, SignInActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }
    }

    override fun setHeader() {
    }

    override val bindingVariable: Int
        get() = BR.userSelectionVM

    override val layoutId: Int
        get() = R.layout.activity_user_selection

    override val viewModel: UserSelectionVM
        get() {
            userSelectionVM = ViewModelProvider(this).get(UserSelectionVM::class.java)
            return userSelectionVM
        }

    override fun setObservers() {
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun onCreateAccount() {
        // Checking which user is selected on button click
        when (userSelected) {
            0 -> {
                AlertDialogUtils.showSnakeBar(
                    "Please select one of the user",
                    snackBar,
                    this@UserSelectionActivity
                )
            }
            1 -> {
//                AlertDialogUtils.showSnakeBar("Agent",snackBar,this@UserSelectionActivity)
                val intent = Intent(this, SignUpActivity::class.java)
                //Setting value to session
                RentalHomesApp.appSessionManager.setInt(
                    this,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_USER_TYPE,
                    AGENT
                )
                intent.putExtra(USER_SELECTED, AGENT)// 1 = Agent Selected
                startActivity(intent)
            }
            2 -> {
//                AlertDialogUtils.showSnakeBar("Buyer",snackBar,this@UserSelectionActivity)
                val intent = Intent(this, BuyerSignUpActivity::class.java)
                //Setting value to session
                RentalHomesApp.appSessionManager.setInt(
                    this,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_USER_TYPE,
                    BUYER
                )
                intent.putExtra(USER_SELECTED, BUYER)// 2 = Buyer Selected
                startActivity(intent)
            }
        }
    }

    override fun onSelectionClick(selection: Int) {
        if (selection == 0) {
            /*GlobalMethods.enableButton(this@UserSelectionActivity, binding.btnCreateAccount)
            binding.btnCreateAccount.setTextColor(this.getColor(R.color.white))
            binding.btnCreateAccount.isClickable = true*/
            binding.tvAgentSelect.setBackgroundResource(R.drawable.user_select_checked_signup)
            binding.tvBuyerSelect.setBackgroundResource(R.drawable.user_select_unchecked)
            userSelected = 1
        } else {
           /* GlobalMethods.enableButton(this@UserSelectionActivity, binding.btnCreateAccount)
            binding.btnCreateAccount.setTextColor(this.getColor(R.color.white))
            binding.btnCreateAccount.isClickable = true*/
            binding.tvAgentSelect.setBackgroundResource(R.drawable.user_select_unchecked)
            binding.tvBuyerSelect.setBackgroundResource(R.drawable.user_select_checked_signup)
            userSelected = 2
        }
    }
}