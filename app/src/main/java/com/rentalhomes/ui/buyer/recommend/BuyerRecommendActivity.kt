package com.rentalhomes.ui.buyer.recommend

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.model.responseModel.GetRecommendListBuyerResponse
import com.rentalhomes.databinding.ActivityBuyerRecommendBinding
import com.rentalhomes.ui.agent.propertydetail.PropertyDetailActivity
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods

/**
 * Created by Malhar
 * */
class BuyerRecommendActivity : BaseActivity<ActivityBuyerRecommendBinding, BuyerRecommendVM>(),
    BuyerRecommendNavigator {
    private lateinit var binding: ActivityBuyerRecommendBinding
    private lateinit var buyerRecommendVM: BuyerRecommendVM
    private lateinit var snackBar: View
    private lateinit var propertyList: ArrayList<GetRecommendListBuyerResponse.Data>
    private lateinit var buyerRecommendAdapter: BuyerRecommendAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setHeader()

        binding.swipeToRefresh.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.color_green),
            ContextCompat.getColor(this, R.color.color_yellow),
            ContextCompat.getColor(this, R.color.color_green)
        )

        binding.swipeToRefresh.setOnRefreshListener {
            if (!buyerRecommendVM.isLoading) {
                buyerRecommendVM.isSwipeLoading = true
                buyerRecommendVM.isLoading = false
                buyerRecommendVM.isMoreItemAvailable = true
                buyerRecommendVM.pageIndex = 0
                setData()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setData()
        setObservers()
    }
    private fun init() {
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)
        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this@BuyerRecommendActivity
            it.buyerRecommendVM = buyerRecommendVM
        }
        buyerRecommendVM.attachContext(this)
        buyerRecommendVM.setNavigator(this)
        snackBar = findViewById(android.R.id.content)
    }

    private fun setData() {
        propertyList = ArrayList()
        buyerRecommendVM.getPropertyListCall()
        buyerRecommendAdapter = BuyerRecommendAdapter(this, propertyList, this)
        binding.rvRecommend.adapter = buyerRecommendAdapter
    }

    override fun setHeader() {
        binding.headerBar.tvTitle.text = getString(R.string.recommendations)
        binding.headerBar.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.headerBar.ivInfo.visibility = View.VISIBLE
        binding.headerBar.ivInfo.setOnClickListener {
            GlobalMethods.getTooltipTop(
                this,
                binding.headerBar.ivInfo,
                getString(R.string.buyer_recommend_note)
            )
        }
    }

    override val bindingVariable: Int
        get() = BR.buyerRecommendVM
    override val layoutId: Int
        get() = R.layout.activity_buyer_recommend
    override val viewModel: BuyerRecommendVM
        get() {
            buyerRecommendVM = ViewModelProvider(this).get(BuyerRecommendVM::class.java)
            return buyerRecommendVM
        }

    override fun setObservers() {
        buyerRecommendVM.mldRecommendList.observe(this) { responseList ->
            if (responseList.size == 0 || responseList.isEmpty()) {
                binding.ivNoData.visibility = View.VISIBLE
//                binding.rvRecommend.visibility = View.GONE
                binding.swipeToRefresh.visibility = View.GONE
            } else {
                binding.ivNoData.visibility = View.GONE
//                binding.rvRecommend.visibility = View.VISIBLE
                binding.swipeToRefresh.visibility = View.VISIBLE
            }
            buyerRecommendAdapter.updateList(responseList)
            propertyList = responseList
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

    override fun onItemClick(data: GetRecommendListBuyerResponse.Data) {
        val descIntent = Intent(this, PropertyDetailActivity::class.java)
        descIntent.putExtra(Keys.TYPE, "1")
        descIntent.putExtra(Keys.BUYER_RECOMMENDATION, data)
        startActivity(descIntent)
    }

    override fun hideSwipeLoading() {
        binding.swipeToRefresh.isRefreshing = false
        binding.rvRecommend.scrollToPosition(0)
    }
}