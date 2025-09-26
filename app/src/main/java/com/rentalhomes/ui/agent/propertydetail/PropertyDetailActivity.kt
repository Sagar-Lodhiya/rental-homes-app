package com.rentalhomes.ui.agent.propertydetail

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.Keys.ADD_RECOMMEND_PROPERTY
import com.rentalhomes.data.network.Keys.IGNORE_RECOMMEND_PROPERTY
import com.rentalhomes.data.network.Keys.PROPERTY_ADDRESS
import com.rentalhomes.data.network.Keys.PROPERTY_CITY
import com.rentalhomes.data.network.Keys.PROPERTY_ID
import com.rentalhomes.data.network.Keys.PROPERTY_QR
import com.rentalhomes.data.network.model.responseModel.GetAgentPropertyListResponse
import com.rentalhomes.data.network.model.responseModel.GetRecommendListBuyerResponse
import com.rentalhomes.databinding.ActivityPropertyDetailBinding
import com.rentalhomes.databinding.BottomsheetChangePropertyStatusBinding
import com.rentalhomes.ui.agent.recommend.RecommendActivity
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods

class PropertyDetailActivity : BaseActivity<ActivityPropertyDetailBinding, PropertyDetailVM>(),
    PropertyDetailNavigator {

    private lateinit var binding: ActivityPropertyDetailBinding
    private lateinit var snackBar: View
    private lateinit var propertyDetailVM: PropertyDetailVM
    lateinit var propertyData: GetAgentPropertyListResponse.Data
    lateinit var buyerRecommendList: GetRecommendListBuyerResponse.Data
    var type: String = ""
    private var propertyStatus: Int = 0
    private var propertyId: Int = 0

    //BottomSheet
    private lateinit var bindProStatus: BottomsheetChangePropertyStatusBinding
    private lateinit var proStatusBottomSheet: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setOnClickListener()
        setData()
    }

    private fun setOnClickListener() {
        binding.btnAddProperty.setOnClickListener {
            propertyDetailVM.addIgnorePropertyCall(
                buyerRecommendList.agentDetails?.agentId!!,
                buyerRecommendList.propertyId,
                ADD_RECOMMEND_PROPERTY
            )
        }

        binding.btnRemove.setOnClickListener {
            propertyDetailVM.addIgnorePropertyCall(
                buyerRecommendList.agentDetails?.agentId!!,
                buyerRecommendList.propertyId,
                IGNORE_RECOMMEND_PROPERTY
            )
        }
    }

    private fun setData() {
        //Type is variable to check if data is coming from Non-Listed property list or Agent Home List
        //TYPE = "1" Non-Listed List else Agent Home List
        type = intent.extras?.getString(Keys.TYPE)!!
        if (type == "1") {
            buyerRecommendList = intent.getParcelableExtra(Keys.BUYER_RECOMMENDATION)!!
            Log.e("PROPERTY_DETAIL", "RECOMMEND_INTENT == " + buyerRecommendList.toString())

            propertyId = buyerRecommendList.propertyId
            Glide.with(this).load(buyerRecommendList.propertyImage).dontAnimate()
                .placeholder(R.drawable.ic_proparty_listing_placeholder)
                .error(R.drawable.ic_proparty_listing_placeholder).into(binding.ivProperty)
            binding.tvAddress.text = buyerRecommendList.propertyAddress.toString().replace("\n", "")
            binding.tvCity.text = buyerRecommendList.propertyCity.toString().replace("\n", "")
            binding.tvBed.text = buyerRecommendList.bed.toString()
            binding.tvBath.text = buyerRecommendList.bath.toString()
            binding.tvCar.text = buyerRecommendList.car.toString()
            binding.tvLandMeters.text =
                "${buyerRecommendList.landSize.toString()} ${getString(R.string.meters)}"
            binding.tvSaleStatus.text = buyerRecommendList.propertyStatus.toString()
            Glide.with(this).load(buyerRecommendList.propertyQRImage).into(binding.ivQrCode)
//            binding.tvDescription.text = buyerRecommendList.description.toString()
            binding.btnRecommend.visibility = View.GONE
            binding.btnStatistics.visibility = View.GONE
            binding.tvQuestion.visibility = View.VISIBLE
            binding.llPropertyButtons.visibility = View.VISIBLE

            if (!TextUtils.isEmpty(buyerRecommendList.description.toString())) {
                binding.tvTitleDesc.visibility = View.VISIBLE
                binding.tvDescription.visibility = View.VISIBLE
                binding.tvDescription.text = buyerRecommendList.description.toString()
            }

            when (buyerRecommendList.propertyStatus) {
                resources.getString(R.string.property_status_for_sale) -> propertyStatus = 1
                resources.getString(R.string.property_status_auction) -> propertyStatus = 2
                resources.getString(R.string.property_status_under_offer) -> propertyStatus = 3
                resources.getString(R.string.property_status_under_contract) -> propertyStatus = 4
                resources.getString(R.string.property_status_sold) -> propertyStatus = 5
            }

        } else {
            propertyData = intent.getParcelableExtra(Keys.AGENT_DATA)!!
            Log.e("PROPERTY_DETAIL", "PROPERTY_LIST_INTENT == " + propertyData.toString())

            propertyId = propertyData.propertyId
            Glide.with(this).load(propertyData.propertyImage).dontAnimate()
                .placeholder(R.drawable.ic_proparty_listing_placeholder)
                .error(R.drawable.ic_proparty_listing_placeholder).into(binding.ivProperty)
            binding.tvAddress.text = propertyData.propertyAddress.toString()
            binding.tvCity.text = propertyData.propertyCity.toString()
            binding.tvBed.text = propertyData.bed.toString()
            binding.tvBath.text = propertyData.bath.toString()
            binding.tvCar.text = propertyData.car.toString()
            binding.tvLandMeters.text =
                propertyData.landSize.toString() + " " + getString(R.string.meters)
            binding.tvSaleStatus.text = propertyData.propertyStatus.toString()
            Glide.with(this).load(propertyData.propertyQRImage).into(binding.ivQrCode)
//            binding.tvDescription.text = propertyData.description.toString()
            binding.btnRecommend.visibility = View.VISIBLE
            binding.btnStatistics.visibility = View.VISIBLE
            binding.tvQuestion.visibility = View.GONE
            binding.llPropertyButtons.visibility = View.GONE

            if (!TextUtils.isEmpty(propertyData.description.toString())) {
                binding.tvTitleDesc.visibility = View.VISIBLE
                binding.tvDescription.visibility = View.VISIBLE
                binding.tvDescription.text = propertyData.description.toString()
            }

            when (propertyData.propertyStatus) {
                resources.getString(R.string.property_status_for_sale) -> propertyStatus = 1
                resources.getString(R.string.property_status_auction) -> propertyStatus = 2
                resources.getString(R.string.property_status_under_offer) -> propertyStatus = 3
                resources.getString(R.string.property_status_under_contract) -> propertyStatus = 4
                resources.getString(R.string.property_status_sold) -> propertyStatus = 5
            }
        }

        binding.tvSaleStatus.setOnClickListener {
            proStatusBottomSheet = BottomSheetDialog(this)

            bindProStatus = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.bottomsheet_change_property_status,
                null,
                false
            )

            proStatusBottomSheet.setContentView(bindProStatus.root)

            when (propertyStatus) {
                1 -> {
                    setDefaultTypeFace()
                    bindProStatus.tvForSale.setTypeface(null, Typeface.BOLD)
                    bindProStatus.tvForSale.setTextColor(getColor(R.color.color_green))
                }
                2 -> {
                    setDefaultTypeFace()
                    bindProStatus.tvAuction.setTypeface(null, Typeface.BOLD)
                    bindProStatus.tvAuction.setTextColor(getColor(R.color.color_green))
                }
                3 -> {
                    setDefaultTypeFace()
                    bindProStatus.tvUnderOffer.setTypeface(null, Typeface.BOLD)
                    bindProStatus.tvUnderOffer.setTextColor(getColor(R.color.color_green))
                }
                4 -> {
                    setDefaultTypeFace()
                    bindProStatus.tvUnderContract.setTypeface(null, Typeface.BOLD)
                    bindProStatus.tvUnderContract.setTextColor(getColor(R.color.color_green))
                }
                5 -> {
                    setDefaultTypeFace()
                    bindProStatus.tvSold.setTypeface(null, Typeface.BOLD)
                    bindProStatus.tvSold.setTextColor(getColor(R.color.color_green))
                }
            }

            bindProStatus.tvForSale.setOnClickListener {
                setDefaultTypeFace()
                propertyStatus = 1
                bindProStatus.tvForSale.setTypeface(null, Typeface.BOLD)
                bindProStatus.tvForSale.setTextColor(getColor(R.color.color_green))
                propertyDetailVM.setPropertyStatus(propertyId, propertyStatus)
            }

            bindProStatus.tvAuction.setOnClickListener {
                setDefaultTypeFace()
                propertyStatus = 2
                bindProStatus.tvAuction.setTypeface(null, Typeface.BOLD)
                bindProStatus.tvAuction.setTextColor(getColor(R.color.color_green))
                propertyDetailVM.setPropertyStatus(propertyId, propertyStatus)
            }

            bindProStatus.tvUnderOffer.setOnClickListener {
                setDefaultTypeFace()
                propertyStatus = 3
                bindProStatus.tvUnderOffer.setTypeface(null, Typeface.BOLD)
                bindProStatus.tvUnderOffer.setTextColor(getColor(R.color.color_green))
                propertyDetailVM.setPropertyStatus(propertyId, propertyStatus)
            }

            bindProStatus.tvUnderContract.setOnClickListener {
                setDefaultTypeFace()
                propertyStatus = 4
                bindProStatus.tvUnderContract.setTypeface(null, Typeface.BOLD)
                bindProStatus.tvUnderContract.setTextColor(getColor(R.color.color_green))
                propertyDetailVM.setPropertyStatus(propertyId, propertyStatus)
            }

            bindProStatus.tvSold.setOnClickListener {
                setDefaultTypeFace()
                propertyStatus = 5
                bindProStatus.tvSold.setTypeface(null, Typeface.BOLD)
                bindProStatus.tvSold.setTextColor(getColor(R.color.color_green))
                propertyDetailVM.setPropertyStatus(propertyId, propertyStatus)
            }

            proStatusBottomSheet.show()
        }
    }

    private fun setDefaultTypeFace() {
        bindProStatus.tvForSale.setTypeface(null, Typeface.NORMAL)
        bindProStatus.tvForSale.setTextColor(getColor(R.color.grey_203))
        bindProStatus.tvAuction.setTypeface(null, Typeface.NORMAL)
        bindProStatus.tvAuction.setTextColor(getColor(R.color.grey_203))
        bindProStatus.tvUnderOffer.setTypeface(null, Typeface.NORMAL)
        bindProStatus.tvUnderOffer.setTextColor(getColor(R.color.grey_203))
        bindProStatus.tvUnderContract.setTypeface(null, Typeface.NORMAL)
        bindProStatus.tvUnderContract.setTextColor(getColor(R.color.grey_203))
        bindProStatus.tvSold.setTypeface(null, Typeface.NORMAL)
        bindProStatus.tvSold.setTextColor(getColor(R.color.grey_203))

    }

    private fun init() {
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)
        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this@PropertyDetailActivity
            it.propertyDetailVM = propertyDetailVM
        }
        propertyDetailVM.attachContext(this)
        propertyDetailVM.setNavigator(this)
        snackBar = findViewById(android.R.id.content)
    }

    override fun setHeader() {
    }

    override val bindingVariable: Int
        get() = BR.propertyDetailVM
    override val layoutId: Int
        get() = R.layout.activity_property_detail
    override val viewModel: PropertyDetailVM
        get() {
            propertyDetailVM = ViewModelProvider(this).get(PropertyDetailVM::class.java)
            return propertyDetailVM
        }

    override fun setObservers() {
    }

    override fun onRecommendClick() {
        val recommendIntent = Intent(this, RecommendActivity::class.java)
        recommendIntent.putExtra(PROPERTY_ID, propertyData.propertyId)
        recommendIntent.putExtra(PROPERTY_ADDRESS, propertyData.propertyAddress)
        recommendIntent.putExtra(PROPERTY_CITY, propertyData.propertyCity)
        recommendIntent.putExtra(PROPERTY_QR, propertyData.propertyQRImage)
        startActivity(recommendIntent)
    }

    override fun onBackClick() {
        onBackPressed()
    }

    override fun onStatisticsClick() {

    }

    override fun onQRImageClick() {
        if (type == "1")
            AlertDialogUtils.showQRImagePopup(this, buyerRecommendList.propertyQRImage)
        else
            AlertDialogUtils.showQRImagePopup(this, propertyData.propertyQRImage)

    }

    override fun showProgress() {
        showProgressDialog()
    }

    override fun hideProgress() {
        hideProgressDialog()
    }

    override fun setMessageComingFromServer(message: String?) {
        AlertDialogUtils.showSnakeBar(message, snackBar, this)
    }

    override fun onResponse(message: String?) {
//        Handler(Looper.getMainLooper()).postDelayed({
//            finish()
//        }, 2000)
        AlertDialogUtils.showAlertSignup(this, message) { dialog, _ ->
            finish()
        }
    }

    override fun setPropertyResponse(message: String?) {
        proStatusBottomSheet.dismiss()
        when (propertyStatus) {
            1 -> binding.tvSaleStatus.text = getString(R.string.property_status_for_sale)
            2 -> binding.tvSaleStatus.text = getString(R.string.property_status_auction)
            3 -> binding.tvSaleStatus.text = getString(R.string.property_status_under_offer)
            4 -> binding.tvSaleStatus.text = getString(R.string.property_status_under_contract)
            5 -> binding.tvSaleStatus.text = getString(R.string.property_status_sold)
        }
    }
}