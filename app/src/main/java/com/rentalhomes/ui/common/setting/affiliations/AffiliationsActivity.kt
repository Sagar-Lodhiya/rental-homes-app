package com.rentalhomes.ui.common.setting.affiliations

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.data.network.model.responseModel.GetAffiliationsResponse
import com.rentalhomes.databinding.ActivityAffiliationsBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Dharmesh
 * */
class AffiliationsActivity : BaseActivity<ActivityAffiliationsBinding, AffiliationsVM>(),
    AffiliationsNavigator {
    private lateinit var binding: ActivityAffiliationsBinding
    private lateinit var affiliationsVM: AffiliationsVM
    private lateinit var snackBar: View
    val dummyAffiliationsResponse = GetAffiliationsResponse(
        message = "Affiliations fetched successfully",
        status = 200,
        affiliations = arrayListOf(
            GetAffiliationsResponse.Affiliations(
                image = "https://logo.clearbit.com/housing.com",
                link  = "https://housing.com"
            ),
            GetAffiliationsResponse.Affiliations(
                image = "https://logo.clearbit.com/www.99acres.com",
                link  = "https://www.99acres.com"
            ),
            GetAffiliationsResponse.Affiliations(
                image = "https://logo.clearbit.com/www.magicbricks.com",
                link  = "https://www.magicbricks.com"
            ),
            GetAffiliationsResponse.Affiliations(
                image = "https://logo.clearbit.com/www.nobroker.in",
                link  = "https://www.nobroker.in"
            ),
            GetAffiliationsResponse.Affiliations(
                image = "https://logo.clearbit.com/realtor.com",
                link  = "https://www.realtor.com"
            )
        )
    )

    private var affiliationsList = ArrayList<GetAffiliationsResponse.Affiliations>()
    private lateinit var affiliationsAdapter: AffiliationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setHeader()
        setOnListener()
        setAffiliationsRecyclerView()
        setObservers()

        //API call for Affiliations
        affiliationsVM.getAffiliationsCall()
    }

    //Initialization is done here
    private fun init() {
        //Set status bar color
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)

        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this
            it.affiliationsVM = affiliationsVM
        }
        affiliationsVM.attachContext(this)
        affiliationsVM.setNavigator(this)

        //Initialize snackBar object here
        snackBar = findViewById(android.R.id.content)
    }

    //Data binding
    override val bindingVariable: Int
        get() = BR.affiliationsVM
    override val layoutId: Int
        get() = R.layout.activity_affiliations
    override val viewModel: AffiliationsVM
        get() {
            affiliationsVM = ViewModelProvider(this).get(AffiliationsVM::class.java)
            return affiliationsVM
        }

    override fun setHeader() {
        binding.headerBar.tvTitle.text = resources.getString(R.string.affiliations)
    }

    private fun setOnListener() {
        binding.headerBar.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setAffiliationsRecyclerView() {
        affiliationsList = dummyAffiliationsResponse.affiliations
        affiliationsAdapter = AffiliationsAdapter(this, affiliationsList, this)
        binding.rvAffiliations.apply {
            this.adapter = affiliationsAdapter
        }
    }

    override fun onAffiliationsItemClick(position: Int, link: String) {
        var url = link
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
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

    override fun setObservers() {
        affiliationsVM.mldAffiliations.observe(this) { response ->
            affiliationsAdapter.updateList(response.affiliations)
            if (response.affiliations.isEmpty()) {
                binding.ivNoData.visibility = View.VISIBLE
                binding.rvAffiliations.visibility = View.GONE
            }else{
                binding.ivNoData.visibility = View.GONE
                binding.rvAffiliations.visibility = View.VISIBLE
            }
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }
}