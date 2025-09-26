package com.rentalhomes.ui.agent.recommend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.data.network.Keys.IS_OTHER_USER
import com.rentalhomes.data.network.Keys.OTHER_USER_ID
import com.rentalhomes.data.network.Keys.PROPERTY_ADDRESS
import com.rentalhomes.data.network.Keys.PROPERTY_CITY
import com.rentalhomes.data.network.Keys.PROPERTY_ID
import com.rentalhomes.data.network.Keys.PROPERTY_QR
import com.rentalhomes.data.network.model.responseModel.GetRecommendedAgentResponse
import com.rentalhomes.databinding.ActivityRecommendBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.ui.buyer.profile.BuyerProfileActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods

class RecommendActivity : BaseActivity<ActivityRecommendBinding, RecommendVM>(),
    RecommendNavigator, ProspectNavigator {

    private lateinit var binding: ActivityRecommendBinding
    private lateinit var snackBar: View
    private lateinit var recommendVM: RecommendVM
    private lateinit var recommendAdapter: RecommendAdapter
    private lateinit var prospectsAdapter: ProspectsAdapter
    private lateinit var recommendList: ArrayList<GetRecommendedAgentResponse.Data>
    private lateinit var prospectList: ArrayList<GetRecommendedAgentResponse.Data>
    private var propertyId: Int? = 0
    private var propertyAddress: String? = ""
    private var propertyCity: String? = ""
    private var propertyQR: String? = ""
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setHeader()
        setObservers()
        setRecommendData()
        setProspectsData()
    }


    private fun setProspectsData() {
        prospectList = ArrayList()
        recommendVM.getProspectsList(0)
        prospectsAdapter = ProspectsAdapter(this, prospectList, this)
        binding.rvProspects.adapter = prospectsAdapter
    }

    private fun setRecommendData() {
        recommendList = ArrayList()
        recommendVM.getRecommendList(propertyId!!)
        recommendAdapter = RecommendAdapter(this, recommendList, this)
        binding.rvRecommend.adapter = recommendAdapter
    }

    private fun init() {
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)
        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this@RecommendActivity
            it.recommendVM = recommendVM
        }
        binding.headerBar.tvTitle.text = getString(R.string.recommend)
        recommendVM.attachContext(this)
        recommendVM.setNavigator(this)
        snackBar = findViewById(android.R.id.content)
    }

    override fun setHeader() {
        binding.headerBar.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.headerBar.ivInfo.visibility = VISIBLE
        binding.headerBar.ivInfo.setOnClickListener {
            GlobalMethods.getTooltipTop(
                this,
                binding.headerBar.ivInfo,
                getString(R.string.agent_recommend_note)
            )
        }
    }

    override val bindingVariable: Int
        get() = BR.recommendVM
    override val layoutId: Int
        get() = R.layout.activity_recommend
    override val viewModel: RecommendVM
        get() {
            recommendVM = ViewModelProvider(this).get(RecommendVM::class.java)
            return recommendVM
        }

    override fun setObservers() {
        recommendVM.mldRecommendList.observe(this) { responseList ->
            if (responseList.size == 0 || responseList.isEmpty()) {
                binding.ivNoData.visibility = View.VISIBLE
                binding.rvRecommend.visibility = View.GONE
            } else {
                binding.ivNoData.visibility = View.GONE
                binding.rvRecommend.visibility = View.VISIBLE
            }
            recommendList = responseList
            recommendAdapter.updateList(recommendList)
            Log.e("Recommend", "RecommendList1 == " + recommendList.toString())
        }

        recommendVM.mldProspectsList.observe(this) { responseList ->
            if (responseList.size == 0 || responseList.isEmpty()) {
                binding.ivNoData.visibility = View.VISIBLE
                binding.rvProspects.visibility = View.GONE
            } else {
                binding.ivNoData.visibility = View.GONE
                binding.rvProspects.visibility = View.VISIBLE
            }
            prospectList = responseList
            prospectsAdapter.updateList(prospectList)
            Log.e("Recommend", "ProspectList1 == " + prospectList.toString())
        }
    }

    override fun onRecommendClick() {
        setRecommendData()
        binding.rvProspects.visibility = View.GONE
        binding.rvRecommend.visibility = View.VISIBLE
        binding.tvRecommend.setBackgroundResource(R.drawable.bg_app_theme_button)
        binding.tvRecommend.setTextColor(getColor(R.color.white))
        binding.tvProspects.setTextColor(getColor(R.color.grey_600))
        binding.tvProspects.setBackgroundResource(0)

        //Change header title
        binding.headerBar.tvTitle.text = getString(R.string.recommend)
    }

    override fun onProspectsClick() {
        setProspectsData()
        binding.rvProspects.visibility = View.VISIBLE
        binding.rvRecommend.visibility = View.GONE
        binding.tvRecommend.setBackgroundResource(0)
        binding.tvRecommend.setTextColor(getColor(R.color.grey_600))
        binding.tvProspects.setTextColor(getColor(R.color.white))
        binding.tvProspects.setBackgroundResource(R.drawable.bg_app_theme_button)

        //Change header title
        binding.headerBar.tvTitle.text = getString(R.string.prospects)
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

    override fun likeSuccess(it: String) {
        AlertDialogUtils.showSnakeBar(it, snackBar, this)
        if (prospectList.isNotEmpty())
            prospectList.removeAt(position)
        prospectsAdapter.notifyDataSetChanged()
        if (prospectsAdapter.itemCount == 0) {
            binding.ivNoData.visibility = View.VISIBLE
            binding.rvProspects.visibility = View.GONE
        }
    }

    override fun onLikeClick(buyerId: Int, position: Int) {
        this.position = position
        Toast.makeText(this,"Property Recommended to User Successfully!!!",Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(otherUserId: Int) {
    }

    override fun onUserClick(otherUserId: Int) {

    }
}