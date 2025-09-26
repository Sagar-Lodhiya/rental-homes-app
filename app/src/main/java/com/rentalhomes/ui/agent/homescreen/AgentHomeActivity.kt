package com.rentalhomes.ui.agent.homescreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.model.responseModel.GetAgentPropertyListResponse
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.databinding.ActivityHomeBinding
import com.rentalhomes.ui.agent.profile.ProfileActivity
import com.rentalhomes.ui.agent.propertydetail.PropertyDetailActivity
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.ui.common.addnonlisted.AddNonListedActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods

class AgentHomeActivity : BaseActivity<ActivityHomeBinding, AgentHomeVM>(), View.OnClickListener,
    AgentHomeNavigator {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var snackBar: View
    private lateinit var homeVM: AgentHomeVM
    private lateinit var homeAdapter: AgentHomeAdapter
    private lateinit var propertyListedList: ArrayList<GetAgentPropertyListResponse.Data>
    private lateinit var offMarketList: ArrayList<GetAgentPropertyListResponse.Data>
    private var notificationCountList: ArrayList<String>? = null
    private var chatCount: ArrayList<String>? = null
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

        binding.swipeToRefreshListed.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.color_green),
            ContextCompat.getColor(this, R.color.color_yellow),
            ContextCompat.getColor(this, R.color.color_green)
        )

        binding.swipeToRefreshOffMarket.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.color_green),
            ContextCompat.getColor(this, R.color.color_yellow),
            ContextCompat.getColor(this, R.color.color_green)
        )

        binding.swipeToRefreshListed.isEnabled = true
        binding.swipeToRefreshOffMarket.isEnabled = false

        binding.swipeToRefreshListed.setOnRefreshListener {
            if (!homeVM.isLoading) {
                homeVM.isSwipeLoading = true
                homeVM.isLoading = false
                homeVM.isMoreItemAvailable = true
                homeVM.pageIndex = 0
                homeVM.getAgentHomeList()
            }
        }

        binding.swipeToRefreshOffMarket.setOnRefreshListener {
            if (!homeVM.isLoading) {
                homeVM.isSwipeLoading = true
                homeVM.isLoading = false
                homeVM.isMoreItemAvailable = true
                homeVM.pageIndex = 0
                homeVM.getAgentHomeList()
            }
        }

//        homeVM.getAgentHomeList()
//        setObservers()
        setPrefData()
    }

    private fun setPrefData() {

        Glide.with(this).load("https://randomuser.me/api/portraits/men/75.jpg")
            .into(binding.civProfile)
    }

    override fun onRestart() {
        super.onRestart()
        setPrefData()
    }

    override fun onResume() {
        super.onResume()
        homeVM.getAgentHomeList()
        setObservers()
    }

    private fun init() {
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)
        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this@AgentHomeActivity
            it.homeVM = homeVM
        }
        homeVM.attachContext(this)
        homeVM.setNavigator(this)
        snackBar = findViewById(android.R.id.content)
        db = FirebaseFirestore.getInstance()
    }

    override fun setHeader() {
    }

    override val bindingVariable: Int
        get() = BR.homeVM
    override val layoutId: Int
        get() = R.layout.activity_home
    override val viewModel: AgentHomeVM
        get() {
            homeVM = ViewModelProvider(this).get(AgentHomeVM::class.java)
            return homeVM
        }

    override fun setObservers() {
        homeVM.mldAgentPropertyList.observe(this) { responseList ->
            propertyListedList = ArrayList()
            offMarketList = ArrayList()
            for (i in responseList.indices) {
                when (responseList[i].listType) {
                    1 -> {
                        val data = responseList[i].copy()
                        propertyListedList.add(data)
                    }
                    2 -> {
                        val data = responseList[i].copy()
                        offMarketList.add(data)
                    }
                }
            }
            setAdapter()
        }

    }

    private fun setAdapter() {
        if (flag == 0) {
            if (propertyListedList.isEmpty()) {
                binding.ivNoData.visibility = View.VISIBLE
                binding.rvOffMarket.visibility = View.GONE
                binding.rvListed.visibility = View.GONE
            } else {
                binding.rvOffMarket.visibility = View.GONE
                binding.rvListed.visibility = View.VISIBLE
                binding.ivNoData.visibility = View.GONE
            }

            homeAdapter = AgentHomeAdapter(this, propertyListedList, this)
            binding.rvListed.adapter = homeAdapter
        } else {
            if (offMarketList.isEmpty()) {
                binding.ivNoData.visibility = View.VISIBLE
                binding.rvOffMarket.visibility = View.GONE
                binding.rvListed.visibility = View.GONE
            } else {
                binding.ivNoData.visibility = View.GONE
                binding.rvOffMarket.visibility = View.VISIBLE
                binding.rvListed.visibility = View.GONE
            }

            homeAdapter = AgentHomeAdapter(this, offMarketList, this)
            binding.rvOffMarket.adapter = homeAdapter

        }
    }

    var flag: Int = 0

    override fun onClick(p0: View?) {
    }

    override fun onListTypeSelected(flag: Int) {
        this.flag = flag
        if (flag == 0) {
            binding.rvListed.visibility = View.VISIBLE
            if (propertyListedList.isEmpty()) {
                binding.ivNoData.visibility = View.VISIBLE
                binding.rvOffMarket.visibility = View.GONE
                binding.rvListed.visibility = View.GONE
            } else {
                binding.rvOffMarket.visibility = View.GONE
                binding.rvListed.visibility = View.VISIBLE
                binding.ivNoData.visibility = View.GONE
                binding.swipeToRefreshListed.visibility = View.VISIBLE
                binding.swipeToRefreshOffMarket.visibility = View.GONE
            }

            binding.swipeToRefreshListed.isEnabled = true
            binding.swipeToRefreshOffMarket.isEnabled = false
            binding.swipeToRefreshListed.visibility = View.VISIBLE
            binding.tvListed.setBackgroundResource(R.drawable.bg_app_theme_button)
            binding.tvListed.setTextColor(getColor(R.color.white))
            binding.tvOffMarket.setTextColor(getColor(R.color.grey_600))
            binding.tvOffMarket.setBackgroundResource(0)
            setObservers()
        } else {
            Log.e("agentHome", "onListTypeSelected: ${offMarketList.size}")
            if (offMarketList.isEmpty() || offMarketList.size == 0) {
                binding.ivNoData.visibility = View.VISIBLE
                binding.rvOffMarket.visibility = View.GONE
                binding.rvListed.visibility = View.GONE
            } else {
                binding.ivNoData.visibility = View.GONE
                binding.rvOffMarket.visibility = View.VISIBLE
                binding.rvListed.visibility = View.GONE
                binding.swipeToRefreshOffMarket.visibility = View.VISIBLE
                binding.swipeToRefreshListed.visibility = View.GONE
            }

            binding.swipeToRefreshListed.isEnabled = false
            binding.swipeToRefreshOffMarket.isEnabled = true
            binding.swipeToRefreshOffMarket.visibility = View.VISIBLE
            binding.tvListed.setBackgroundResource(0)
            binding.tvListed.setTextColor(getColor(R.color.grey_600))
            binding.tvOffMarket.setTextColor(getColor(R.color.white))
            binding.tvOffMarket.setBackgroundResource(R.drawable.bg_app_theme_button)
            setObservers()
        }
    }

    //Listener for item click in list
    override fun onListedItemClick(data: GetAgentPropertyListResponse.Data) {
        val descIntent = Intent(this, PropertyDetailActivity::class.java)
        descIntent.putExtra(Keys.TYPE, "0")
        descIntent.putExtra(Keys.AGENT_DATA, data)
        startActivity(descIntent)
    }

    override fun onProfileClicked() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }

    override fun onNotificationClicked() {
//        startActivity(Intent(this, NotificationActivity::class.java))
    }

    override fun onMessageClick() {
//        startActivity(Intent(this, PropertyChatListActivity::class.java))
    }

    override fun onAddNonListedClicked() {
        intent = Intent(this, AddNonListedActivity::class.java)
        intent.putExtra(Keys.TYPE, "0")
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

    override fun hideSwipeLoading() {
//        if (flag == 0) {
        binding.swipeToRefreshListed.isRefreshing = false
        binding.rvListed.scrollToPosition(0)
//        } else if (flag == 1) {
        binding.swipeToRefreshOffMarket.isRefreshing = false
        binding.rvOffMarket.scrollToPosition(0)
//        }
    }
}