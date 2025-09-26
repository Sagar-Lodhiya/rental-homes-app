package com.rentalhomes.ui.buyer.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.Keys.IS_OTHER_USER
import com.rentalhomes.data.network.model.responseModel.GetPropertyListBuyerResponse
import com.rentalhomes.data.pref.AppSessionManager.Companion.PREF_KEY_IS_COMPARE_MODE
import com.rentalhomes.data.pref.AppSessionManager.Companion.PREF_KEY_PROFILE_THUMB
import com.rentalhomes.data.pref.AppSessionManager.Companion.PREF_NAME
import com.rentalhomes.databinding.ActivityBuyerHomeBinding
import com.rentalhomes.databinding.BottomBuyerHomeFilterBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.ui.buyer.profile.BuyerProfileActivity
import com.rentalhomes.ui.buyer.propertydetail.BuyerPropertyDetailActivity
import com.rentalhomes.ui.buyer.recommend.BuyerRecommendActivity
import com.rentalhomes.ui.common.addnonlisted.AddNonListedActivity
import com.rentalhomes.ui.common.qrcode.ScanActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods
import com.rentalhomes.utils.customviews.RegularTextView


class BuyerHomeActivity : BaseActivity<ActivityBuyerHomeBinding, BuyerHomeVM>(),
    BuyerHomeNavigator {
    private lateinit var binding: ActivityBuyerHomeBinding
    private lateinit var buyerHomeVM: BuyerHomeVM
    private lateinit var snackBar: View
    private lateinit var buyerHomeAdapter: BuyerHomeAdapter
    private var notificationCountList: ArrayList<String>? = null
    private var chatCount: ArrayList<String>? = null
    private lateinit var db: FirebaseFirestore
    private var propertyList: ArrayList<GetPropertyListBuyerResponse.Data>? = null

    //    Bottom sheet variables
    private lateinit var bindFilterBottom: BottomBuyerHomeFilterBinding
    private lateinit var filterBottomSheet: BottomSheetDialog

    private var isCompareMode: Boolean = false
    private var infoView: View? = null

    //Set Highest to lowest score as default filter type
    private var filterType = 0

    //To store selected property ids
    private var selectedPropertyId: ArrayList<Int> = ArrayList()

    companion object {
        private val TAG = BuyerHomeActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        generateToken()
        init()
        setObservers()
        setData()
        swipeToRefresh()
//        setonTouchListener()
        setPrefData()
//        setNotificationCount()

        propertyList = arrayListOf(
            GetPropertyListBuyerResponse.Data(
                propertyId = 101,
                propertyType = 1, // House
                propertyImage = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=1080",
                propertyImageThumb = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=400",
                propertyAddress = "123 Palm Street",
                propertyCity = "Mumbai",
                propertyCreatedDate = "2025-09-20",
                propertyQRImage = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=Property101",
                description = "A beautiful 3BHK villa near the beach with garden view.",
                landSize = 2500,
                bed = 3,
                bath = 2,
                car = 1,
                propertyStatus = "Available",
                latitude = "19.0760",
                longitude = "72.8777",
                agentName = "John Doe",
                agentProfilePic = "https://images.unsplash.com/photo-1603415526960-f7e0328c63b1?w=400",
                agentDetails = GetPropertyListBuyerResponse.AgentDetails(
                    agentId = 11,
                    agentName = "John Doe",
                    agentProfilePic = "https://images.unsplash.com/photo-1603415526960-f7e0328c63b1?w=400",
                    agentThumbPic = "https://images.unsplash.com/photo-1603415526960-f7e0328c63b1?w=200",
                    agentMobile = "+91 9876543210",
                    agentEmail = "john.doe@example.com",
                    agentAgency = "Dream Homes Realty"
                ),
                defaultTemplateType = 2,
                isTick = 1,
                ratingScore = 4
            ),
            GetPropertyListBuyerResponse.Data(
                propertyId = 102,
                propertyType = 2, // Apartment
                propertyImage = "https://images.unsplash.com/photo-1568605114967-8130f3a36994?w=1080",
                propertyImageThumb = "https://images.unsplash.com/photo-1568605114967-8130f3a36994?w=400",
                propertyAddress = "45 Sunshine Apartments",
                propertyCity = "Pune",
                propertyCreatedDate = "2025-09-15",
                propertyQRImage = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=Property102",
                description = "Modern 2BHK apartment with pool access and security.",
                landSize = 1200,
                bed = 2,
                bath = 2,
                car = 1,
                propertyStatus = "Sold",
                latitude = "18.5204",
                longitude = "73.8567",
                agentName = "Jane Smith",
                agentProfilePic = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400",
                agentDetails = GetPropertyListBuyerResponse.AgentDetails(
                    agentId = 12,
                    agentName = "Jane Smith",
                    agentProfilePic = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400",
                    agentThumbPic = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200",
                    agentMobile = "+91 9988776655",
                    agentEmail = "jane.smith@example.com",
                    agentAgency = "Urban Living Realty"
                ),
                defaultTemplateType = 1,
                isTick = 0,
                ratingScore = 5
            ),
            GetPropertyListBuyerResponse.Data(
                propertyId = 103,
                propertyType = 3, // Bungalow
                propertyImage = "https://images.unsplash.com/photo-1572120360610-d971b9d7767c?w=1080",
                propertyImageThumb = "https://images.unsplash.com/photo-1572120360610-d971b9d7767c?w=400",
                propertyAddress = "89 Green Valley Bungalows",
                propertyCity = "Bengaluru",
                propertyCreatedDate = "2025-09-10",
                propertyQRImage = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=Property103",
                description = "Spacious bungalow with 4BHK layout, private lawn and large balcony.",
                landSize = 3200,
                bed = 4,
                bath = 3,
                car = 2,
                propertyStatus = "Available",
                latitude = "12.9716",
                longitude = "77.5946",
                agentName = "Rahul Verma",
                agentProfilePic = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400",
                agentDetails = GetPropertyListBuyerResponse.AgentDetails(
                    agentId = 13,
                    agentName = "Rahul Verma",
                    agentProfilePic = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400",
                    agentThumbPic = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200",
                    agentMobile = "+91 9123456789",
                    agentEmail = "rahul.verma@example.com",
                    agentAgency = "Luxury Homes India"
                ),
                defaultTemplateType = 2,
                isTick = 1,
                ratingScore = 3
            ),
            GetPropertyListBuyerResponse.Data(
                propertyId = 104,
                propertyType = 4, // Penthouse
                propertyImage = "https://images.unsplash.com/photo-1505691938895-1758d7feb511?w=1080",
                propertyImageThumb = "https://images.unsplash.com/photo-1505691938895-1758d7feb511?w=400",
                propertyAddress = "Skyline Towers, Penthouse 21",
                propertyCity = "Delhi",
                propertyCreatedDate = "2025-09-05",
                propertyQRImage = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=Property104",
                description = "Luxury 5BHK penthouse with skyline view, jacuzzi and private terrace.",
                landSize = 5000,
                bed = 5,
                bath = 4,
                car = 3,
                propertyStatus = "Available",
                latitude = "28.6139",
                longitude = "77.2090",
                agentName = "Ananya Kapoor",
                agentProfilePic = "https://images.unsplash.com/photo-1547425260-76bcadfb4f2c?w=400",
                agentDetails = GetPropertyListBuyerResponse.AgentDetails(
                    agentId = 14,
                    agentName = "Ananya Kapoor",
                    agentProfilePic = "https://images.unsplash.com/photo-1547425260-76bcadfb4f2c?w=400",
                    agentThumbPic = "https://images.unsplash.com/photo-1547425260-76bcadfb4f2c?w=200",
                    agentMobile = "+91 9090909090",
                    agentEmail = "ananya.kapoor@example.com",
                    agentAgency = "Skyline Realty"
                ),
                defaultTemplateType = 3,
                isTick = 1,
                ratingScore = 5
            )
        )


    }

    private fun swipeToRefresh() {

        binding.swipeToRefresh.setOnRefreshListener {
            if (!buyerHomeVM.isLoading) {
                buyerHomeVM.isSwipeLoading = true
                buyerHomeVM.isLoading = false
                buyerHomeVM.isMoreItemAvailable = true
                buyerHomeVM.pageIndex = 0
                onResume()
            }
        }

        binding.swipeToRefresh.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.color_green),
            ContextCompat.getColor(this, R.color.color_yellow),
            ContextCompat.getColor(this, R.color.color_green)
        )
    }

//    private fun setNotificationCount() {
//        val fdb: Query
//        val userType: Int? = RentalHomesApp.appSessionManager.getInt(
//            this,
//            PREF_NAME,
//            PREF_KEY_USER_TYPE
//        )
//        val userId: Int? = RentalHomesApp.appSessionManager.getInt(
//            this,
//            PREF_NAME,
//            PREF_KEY_USER_ID
//        )
//        fdb = db.collection(USERS).whereEqualTo(Keys.USER_ID, userId.toString())
//        fdb.addSnapshotListener(EventListener { snapshots, error ->
//            if (error != null) {
//                error.printStackTrace()
//                return@EventListener
//            }
//
//            snapshots?.let { snapshot ->
//                snapshot.documentChanges.forEach { documentChange ->
//                    notificationCountList =
//                        (documentChange.document.data.get(Constant.NOTIFICATION_COUNT) as ArrayList<String>?)!!
//                    chatCount =
//                        (documentChange.document.data.get(Constant.CHAT_COUNT) as ArrayList<String>?)!!
//                    Log.e(
//                        "BUYERHOME",
//                        "NotificationListCount " + chatCount.toString() + "==SIZE== " + chatCount?.size
//                    )
//                    if (notificationCountList?.size!! > 0 || notificationCountList!!.isNotEmpty()) {
//                        binding.ivNotification.setImageResource(R.drawable.notification_dot)
//                    } else {
//                        binding.ivNotification.setImageResource(R.drawable.ic_notification)
//                    }
//                    if (chatCount?.size!! > 0) {
//                        binding.ivChat.setImageResource(R.drawable.chat_count)
//                    } else {
//                        binding.ivChat.setImageResource(R.drawable.ic_chat)
//                    }
//                }
//            }
//        })
//
//
//    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setonTouchListener() {
        binding.mainLayout.setOnTouchListener { v, event ->
//            if (infoView != null) {
//                infoView!!.visibility = View.GONE
//                infoView = null
//            }
            v?.onTouchEvent(event) ?: true
        }

    }

    private fun init() {
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)
        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this@BuyerHomeActivity
            it.buyerHomeVM = buyerHomeVM
        }
        buyerHomeVM.attachContext(this)
        buyerHomeVM.setNavigator(this)
        snackBar = findViewById(android.R.id.content)
//        db = FirebaseFirestore.getInstance()
    }

    override val bindingVariable: Int
        get() = BR.buyerHomeVM
    override val layoutId: Int
        get() = R.layout.activity_buyer_home
    override val viewModel: BuyerHomeVM
        get() {
            buyerHomeVM = ViewModelProvider(this).get(BuyerHomeVM::class.java)
            return buyerHomeVM
        }

    private fun setPrefData() {
        val thumbnailRequest = Glide.with(this)
            .load(RentalHomesApp.appSessionManager.getString(this, PREF_NAME, PREF_KEY_PROFILE_THUMB))

        Glide.with(this)
            .load("https://randomuser.me/api/portraits/men/75.jpg")
            .thumbnail(thumbnailRequest)
            .placeholder(R.drawable.ic_profile_place_holder)
            .into(binding.civProfile)
    }

    override fun onRestart() {
        super.onRestart()
        setPrefData()
    }

    override fun onResume() {
        super.onResume()
//        buyerHomeVM.getHomePropertyListCall(filterType)
//        setNotificationCount()
        //Check compare mode is ON or Off, set from compare property activity on back pressed and add property click
        if (RentalHomesApp.appSessionManager.getBoolean(
                this,
                PREF_NAME,
                PREF_KEY_IS_COMPARE_MODE
            ) == true
        ) {
            for (i in 0 until selectedPropertyId.size) {
                for (j in 0 until propertyList!!.size) {
                    if (selectedPropertyId[i] == propertyList!![j].propertyId) {
                        propertyList!![j].isTick = 1
                        Log.e(TAG, "selected property ID: " + propertyList!![j].propertyId)
                    }
                }
            }
            setRecyclerView()
        } else {
            isCompareMode = false
            binding.tvCompareNow.visibility = View.GONE
            binding.fBtnCompare.visibility = View.VISIBLE
            setRecyclerView()
            selectedPropertyId.clear()
            for (i in 0 until propertyList!!.size) {
                propertyList!![i].isTick = 0
            }
        }
    }

    override fun onBackPressed() {
        if (isCompareMode) {
            isCompareMode = false
            binding.tvCompareNow.visibility = View.GONE
            binding.fBtnCompare.visibility = View.VISIBLE
            setRecyclerView()
            selectedPropertyId.clear()
            for (i in 0 until propertyList!!.size) {
                propertyList!![i].isTick = 0
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun setData() {
        propertyList = ArrayList()
//        buyerHomeVM.getHomePropertyListCall()

    }

    private fun setRecyclerView() {
        binding.rvBuyerHomeList.itemAnimator = null
        buyerHomeAdapter = BuyerHomeAdapter(this, propertyList!!, this, isCompareMode)
        binding.rvBuyerHomeList.adapter = buyerHomeAdapter
    }

    override fun setHeader() {
    }

    override fun setObservers() {
        buyerHomeVM.mldPropertyList.observe(this) { responseList ->
            if (responseList.size == 0 || responseList.isEmpty()) {
                binding.ivNoData.visibility = View.VISIBLE
            } else {
                binding.ivNoData.visibility = View.GONE
                binding.swipeToRefresh.visibility = View.VISIBLE
            }
            buyerHomeAdapter.updateList(responseList)
            propertyList = responseList

            //To mark Tick when add property
            for (i in 0 until selectedPropertyId.size) {
                for (j in 0 until propertyList!!.size) {
                    if (selectedPropertyId[i] == propertyList!![j].propertyId) {
                        propertyList!![j].isTick = 1
                        Log.e(TAG, "selected property ID: " + propertyList!![j].propertyId)
                    }
                }
            }
            setRecyclerView()
        }
    }

    override fun onNotificationClick() {

    }

    override fun onFilterClick() {
//        startActivity(Intent(this,SettingActivity::class.java))
        showFilterBottomSheet()
    }

    override fun onMessageClick() {

    }

    override fun onMapViewClick() {

    }

    override fun onScanQRClick() {
        intent = Intent(this, ScanActivity::class.java)
        intent.putExtra(Keys.FROM_ACTIVITY, Keys.BUYER_HOME_ACTIVITY)
        startActivity(intent)
    }

    override fun onItemClick(data: GetPropertyListBuyerResponse.Data) {
        val descIntent = Intent(this, BuyerPropertyDetailActivity::class.java)
        descIntent.putExtra("BUYER_LIST", data)
        descIntent.putExtra(Keys.TYPE, "0")
        startActivity(descIntent)
    }

    override fun onCompareClick() {
        if (!isCompareMode) {
            isCompareMode = true
            binding.fBtnCompare.visibility = View.GONE
            binding.tvCompareNow.visibility = View.VISIBLE
//            setData()
            buyerHomeAdapter = BuyerHomeAdapter(this, propertyList!!, this, isCompareMode)
            binding.rvBuyerHomeList.adapter = buyerHomeAdapter
        } /*else {
            startActivity(Intent(this, CompareActivity::class.java))
        }*/
    }

    override fun onTickCountZero() {
        isCompareMode = false
        binding.fBtnCompare.visibility = View.VISIBLE
        binding.tvCompareNow.visibility = View.GONE
//        setData()
        buyerHomeAdapter = BuyerHomeAdapter(this, propertyList!!, this, isCompareMode)
        binding.rvBuyerHomeList.adapter = buyerHomeAdapter
    }

    override fun onCompareNowClick() {
        selectedPropertyId.clear()
        for (i in 0 until propertyList!!.size) {
            if (propertyList!![i].isTick == 1) {
                selectedPropertyId.add(propertyList!![i].propertyId!!)
            }
        }

        if (selectedPropertyId.size > 1) {

        } else {
            AlertDialogUtils.showAlert(
                this,
                getString(R.string.msg_compare_properties_selection)
            )
        }
    }

    override fun onPropertyInfoClick() {
        GlobalMethods.getTooltipTop(
            this,
            binding.ivInfo,
            getString(R.string.buyer_home_note)
        )
    }

    override fun onRecommendClick() {
        startActivity(Intent(this, BuyerRecommendActivity::class.java))
    }

    override fun onNonListedClick() {
//        startActivity(Intent(this, NonListedActivity::class.java))

        intent = Intent(this, AddNonListedActivity::class.java)
        intent.putExtra(Keys.TYPE, "1")
        startActivity(intent)
    }

    override fun onProfileClick() {
        val intent = Intent(this, BuyerProfileActivity::class.java)
        intent.putExtra(IS_OTHER_USER, false)
        startActivity(intent)
    }

    //Filter BottomSheet
    private fun showFilterBottomSheet() {
        //Create instance of BottomSheetDialog
        filterBottomSheet = BottomSheetDialog(this)

        //Binding BottomSheet
        bindFilterBottom = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.bottom_buyer_home_filter,
            null,
            false
        )
        filterBottomSheet.setContentView(bindFilterBottom.root)

        // Statically setting the most recent radio button to default
//        bindFilterBottom.rbMostRecent.isChecked = true
//        bindFilterBottom.rbMostRecent.setTextColor(getColor(R.color.color_green))

        when (filterType) {
            0 -> {
                bindFilterBottom.rbHighToLowScore.isChecked = true
                bindFilterBottom.rbHighToLowScore.setTextColor(getColor(R.color.color_green))
                bindFilterBottom.rbMostRecent.setTextColor(getColor(R.color.black))
                bindFilterBottom.rbHighToLow.setTextColor(getColor(R.color.black))
                bindFilterBottom.rbLowToHigh.setTextColor(getColor(R.color.black))
            }
            1 -> {
                bindFilterBottom.rbMostRecent.isChecked = true
                bindFilterBottom.rbHighToLowScore.setTextColor(getColor(R.color.black))
                bindFilterBottom.rbMostRecent.setTextColor(getColor(R.color.color_green))
                bindFilterBottom.rbHighToLow.setTextColor(getColor(R.color.black))
                bindFilterBottom.rbLowToHigh.setTextColor(getColor(R.color.black))
            }
            2 -> {
                bindFilterBottom.rbHighToLow.isChecked = true
                bindFilterBottom.rbHighToLowScore.setTextColor(getColor(R.color.black))
                bindFilterBottom.rbMostRecent.setTextColor(getColor(R.color.black))
                bindFilterBottom.rbHighToLow.setTextColor(getColor(R.color.color_green))
                bindFilterBottom.rbLowToHigh.setTextColor(getColor(R.color.black))
            }
            3 -> {
                bindFilterBottom.rbLowToHigh.isChecked = true
                bindFilterBottom.rbHighToLowScore.setTextColor(getColor(R.color.black))
                bindFilterBottom.rbMostRecent.setTextColor(getColor(R.color.black))
                bindFilterBottom.rbHighToLow.setTextColor(getColor(R.color.black))
                bindFilterBottom.rbLowToHigh.setTextColor(getColor(R.color.color_green))
            }
        }

        // Radio button click listener
        bindFilterBottom.rgFilter.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rbHighToLowScore -> {
                    filterType = 0
                    bindFilterBottom.rbHighToLowScore.setTextColor(getColor(R.color.color_green))
                    bindFilterBottom.rbMostRecent.setTextColor(getColor(R.color.black))
                    bindFilterBottom.rbHighToLow.setTextColor(getColor(R.color.black))
                    bindFilterBottom.rbLowToHigh.setTextColor(getColor(R.color.black))
                    buyerHomeVM.getHomePropertyListCall(filterType)
                    filterBottomSheet.dismiss()
                }
                R.id.rbMostRecent -> {
                    filterType = 1
                    bindFilterBottom.rbHighToLowScore.setTextColor(getColor(R.color.black))
                    bindFilterBottom.rbMostRecent.setTextColor(getColor(R.color.color_green))
                    bindFilterBottom.rbHighToLow.setTextColor(getColor(R.color.black))
                    bindFilterBottom.rbLowToHigh.setTextColor(getColor(R.color.black))
                    buyerHomeVM.getHomePropertyListCall(filterType)
                    filterBottomSheet.dismiss()
                }
                R.id.rbHighToLow -> {
                    filterType = 2
                    bindFilterBottom.rbHighToLowScore.setTextColor(getColor(R.color.black))
                    bindFilterBottom.rbMostRecent.setTextColor(getColor(R.color.black))
                    bindFilterBottom.rbHighToLow.setTextColor(getColor(R.color.color_green))
                    bindFilterBottom.rbLowToHigh.setTextColor(getColor(R.color.black))
                    buyerHomeVM.getHomePropertyListCall(filterType)
                    filterBottomSheet.dismiss()
                }
                R.id.rbLowToHigh -> {
                    filterType = 3
                    bindFilterBottom.rbHighToLowScore.setTextColor(getColor(R.color.black))
                    bindFilterBottom.rbMostRecent.setTextColor(getColor(R.color.black))
                    bindFilterBottom.rbHighToLow.setTextColor(getColor(R.color.black))
                    bindFilterBottom.rbLowToHigh.setTextColor(getColor(R.color.color_green))
                    buyerHomeVM.getHomePropertyListCall(filterType)
                    filterBottomSheet.dismiss()
                }
            }
        }

        bindFilterBottom.ivClose.setOnClickListener {
            filterBottomSheet.dismiss()
        }
        filterBottomSheet.show()
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
        binding.swipeToRefresh.isRefreshing = false
        binding.rvBuyerHomeList.scrollToPosition(0)
    }

    override fun onDeletePropertyClick(listItem: GetPropertyListBuyerResponse.Data) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_dialog_logout, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()

        val tvMessage: RegularTextView = dialogView.findViewById(R.id.tvMessage)
        val tvDelete: AppCompatButton = dialogView.findViewById(R.id.btnLogout)

        tvMessage.text = getString(R.string.msg_delete_property)
        tvDelete.text = getString(R.string.delete)

        //Cancel button click
        dialogView.findViewById<AppCompatButton>(R.id.btnCancel).setOnClickListener {
            alertDialog.dismiss()
        }

        //Logout button click
        dialogView.findViewById<AppCompatButton>(R.id.btnLogout).setOnClickListener {
            alertDialog.dismiss()
            buyerHomeVM.deletePropertyCall(listItem.propertyId!!)
            propertyList!!.remove(listItem)
//            buyerHomeAdapter.notifyDataSetChanged()
        }

        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    override fun onDeletePropertySuccess() {
        //Re-Initialized adapter and set recyclerview to refresh list
        setRecyclerView()
        if (propertyList!!.size == 0 || propertyList!!.isEmpty()) {
            binding.ivNoData.visibility = View.VISIBLE
        } else {
            binding.ivNoData.visibility = View.GONE
        }
    }

    override fun onEditPropertyClick(listItem: GetPropertyListBuyerResponse.Data) {
        intent = Intent(this, AddNonListedActivity::class.java)
        intent.putExtra(Keys.TYPE, "2")
        intent.putExtra(Keys.NON_LISTED, listItem)
        startActivity(intent)
    }

    private fun generateToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            it?.let {
                Log.e(TAG, "firebase token: " + it)
                RentalHomesApp.appSessionManager.setDeviceToken(this, it)
            }
        }
    }
}