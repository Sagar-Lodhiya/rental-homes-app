package com.rentalhomes.ui.agent.buyerFeedback

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.model.responseModel.GetAgentPropertyListResponse
import com.rentalhomes.data.network.model.responseModel.RatingDataResponse
import com.rentalhomes.databinding.ActivityBuyerFeedbackBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.ui.buyer.profile.BuyerProfileActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods
import com.rentalhomes.utils.customviews.RegularTextView


class BuyerFeedbackActivity : BaseActivity<ActivityBuyerFeedbackBinding, BuyerFeedbackVM>(),
    BuyerFeedbackNavigator {

    private lateinit var binding: ActivityBuyerFeedbackBinding
    private lateinit var buyerFeedbackVM: BuyerFeedbackVM
    private lateinit var snackBar: View

    private var buyerId: Int = 0
    private var tab: Int = 0
    private var propertyData: GetAgentPropertyListResponse.Data? = null
    private var ratingScoreList: ArrayList<BuyerFeedbackModel>? = null
    private var featuresList: ArrayList<RatingDataResponse.Tier3Data>? = null

    private lateinit var boldTypeface: Typeface
    private lateinit var regularTypeface: Typeface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setHeader()
        initFont()

        propertyData = intent.getParcelableExtra(Keys.AGENT_DATA)!!
        buyerId = intent.extras!!.getInt(Keys.BUYER_ID)
        if (buyerId != 0 && propertyData != null) {
            Glide.with(this).load(propertyData!!.propertyQRImage).into(binding.ivQrCode)
            binding.tvAddress.text = propertyData!!.propertyAddress!!.replace("\n", " ")
            binding.tvCity.text = propertyData!!.propertyCity!!.replace("\n", " ")

//            API Call
            buyerFeedbackVM.getRatingData(buyerId, propertyData!!.propertyId)
            setData()
            onFeedbackClicked()
        }
    }

    private fun setData() {
        var que4List: java.util.ArrayList<String> = java.util.ArrayList()
        buyerFeedbackVM.ratingData.observe(this) { data ->
            data?.let {
                if (tab == 1) {
                    if (it.feedbackData != null) {
                        Glide.with(this).load(it.feedbackData.buyerImage).dontAnimate()
                            .placeholder(R.drawable.ic_profile_place_holder)
                            .error(R.drawable.ic_profile_place_holder).into(binding.ivUser)

                        if (!TextUtils.isEmpty(it.feedbackData.buyerName) &&
                            !TextUtils.isEmpty(it.feedbackData.dateTime)/* &&
                                        !TextUtils.isEmpty(it.feedbackData.feedback)*/
                        ) {
                            binding.tvUserName.text = it.feedbackData.buyerName
                            binding.tvDateTime.text = it.feedbackData.dateTime
                            binding.tvFeedback.text = it.feedbackData.feedback
                        } else {
                            binding.ivUser.visibility = View.GONE
                            binding.tvUserName.visibility = View.GONE
                            binding.tvDateTime.visibility = View.GONE
                            binding.tvFeedback.visibility = View.GONE
                            binding.ivNoData.visibility = View.VISIBLE
                        }
                        if (it.feedbackData.feedback == "")
                            binding.tvFeedback.visibility = View.GONE
                        if (it.feedbackData.questions1 == 1) {
                            enableButton(binding.tvQuestion1Yes)
                            disableButton(binding.tvQuestion1No)
                        } else if (it.feedbackData.questions1 == 0) {
                            enableButton(binding.tvQuestion1No)
                            disableButton(binding.tvQuestion1Yes)
                        }
                        if (it.feedbackData.questions2 == 1) {
                            enableButton(binding.tvQuestion2Yes)
                            disableButton(binding.tvQuestion2No)
                        } else if (it.feedbackData.questions2 == 0) {
                            enableButton(binding.tvQuestion2No)
                            disableButton(binding.tvQuestion2Yes)
                        }

                        if (!TextUtils.isEmpty(it.feedbackData.questions4)) {
                            if (!it.feedbackData.questions4.contains(",")) {
                                que4List.add(it.feedbackData.questions4)
                            } else {
                                if (it.feedbackData.questions4.length == 2 &&
                                    it.feedbackData.questions4.startsWith(",") ||
                                    it.feedbackData.questions4.endsWith(",")
                                ) {
                                    que4List.add(it.feedbackData.questions4.replace(",", ""))
                                } else {
                                    que4List =
                                        GlobalMethods.stringToWords(it.feedbackData.questions4)
                                }
                            }

                            if (que4List.isNotEmpty()) {
                                for (i in que4List.indices) {
                                    when (que4List[i]) {
                                        "0" -> {
                                            enableButton(binding.tvQuestion4Email)
                                        }
                                        "1" -> {
                                            enableButton(binding.tvQuestion4Phone)
                                        }
                                        "2" -> {
                                            enableButton(binding.tvQuestion4PropareMsg)
                                        }
                                    }
                                }
                            }
                        }

                        binding.tvUserName.setOnClickListener { binding.ivUser.performClick() }

                        binding.ivUser.setOnClickListener {
                            val intent = Intent(this, BuyerProfileActivity::class.java)
                            intent.putExtra(Keys.IS_OTHER_USER, true)
                            intent.putExtra(Keys.OTHER_USER_ID, buyerId)
                            startActivity(intent)
                        }
                    } else {
                        binding.ivUser.visibility = View.GONE
                        binding.tvUserName.visibility = View.GONE
                        binding.tvDateTime.visibility = View.GONE
                        binding.tvFeedback.visibility = View.GONE
                        binding.ivNoData.visibility = View.VISIBLE
                    }
                } else if (tab == 2) {
                    if (it.tier1 != null) {
                        ratingScoreList = ArrayList()

                        ratingScoreList!!.add(
                            BuyerFeedbackModel(
                                getString(R.string.location),
                                it.tier1.location
                            )
                        )
                        ratingScoreList!!.add(
                            BuyerFeedbackModel(
                                getString(R.string.street_appeal_neighbours),
                                it.tier1.streetAppeal
                            )
                        )
                        ratingScoreList!!.add(
                            BuyerFeedbackModel(
                                getString(R.string.internal_layout_functionality),
                                it.tier1.internalLayout
                            )
                        )
                        ratingScoreList!!.add(
                            BuyerFeedbackModel(
                                getString(R.string.external_layout_functionality),
                                it.tier1.externalLayout
                            )
                        )
                        ratingScoreList!!.add(
                            BuyerFeedbackModel(
                                getString(R.string.quality_of_building_fittings),
                                it.tier1.qualityOfBuilding
                            )
                        )

                        val buyerFeedbackAdapter = BuyerFeedbackAdapter(this, ratingScoreList!!)
                        binding.rvTier1.apply {
                            this.adapter = buyerFeedbackAdapter
                        }

                        if (it.tier1.marketValue != "")
                            binding.tvMarketValue.text =
                                "$${numberFormat(it.tier1.marketValue.toInt())}"
                        else
                            binding.tvMarketValue.text = "$0"
                        /*binding.tvMarketValue.text =
                                            GlobalMethods.stringToPrice(it.tier1.marketValue.toString())*/
                    }
                } else if (tab == 3) {
                    if (it.tier2 != null) {
                        ratingScoreList = ArrayList()

                        if (it.tier2.kitchen != 100)
                            ratingScoreList!!.add(
                                BuyerFeedbackModel(
                                    getString(R.string.kitchen),
                                    it.tier2.kitchen
                                )
                            )
                        if (it.tier2.livingAreas != 100)
                            ratingScoreList!!.add(
                                BuyerFeedbackModel(
                                    getString(R.string.living_areas),
                                    it.tier2.livingAreas
                                )
                            )
                        if (it.tier2.bedroom1 != 100)
                            ratingScoreList!!.add(
                                BuyerFeedbackModel(
                                    getString(R.string.master_bedroom_1),
                                    it.tier2.bedroom1
                                )
                            )
                        if (it.tier2.bedroom2 != 100)
                            ratingScoreList!!.add(
                                BuyerFeedbackModel(
                                    getString(R.string.bedroom_2),
                                    it.tier2.bedroom2
                                )
                            )
                        if (it.tier2.bedroom3 != 100)
                            ratingScoreList!!.add(
                                BuyerFeedbackModel(
                                    getString(R.string.bedroom_3),
                                    it.tier2.bedroom3
                                )
                            )
                        if (it.tier2.bedroom4 != 100)
                            ratingScoreList!!.add(
                                BuyerFeedbackModel(
                                    getString(R.string.bedroom_4_others),
                                    it.tier2.bedroom4
                                )
                            )
                        if (it.tier2.bathrooms != 100)
                            ratingScoreList!!.add(
                                BuyerFeedbackModel(
                                    getString(R.string.bathrooms),
                                    it.tier2.bathrooms
                                )
                            )
                        if (it.tier2.mediaRoom != 100)
                            ratingScoreList!!.add(
                                BuyerFeedbackModel(
                                    getString(R.string.media_ent_room),
                                    it.tier2.mediaRoom
                                )
                            )
                        if (it.tier2.laundry != 100)
                            ratingScoreList!!.add(
                                BuyerFeedbackModel(
                                    getString(R.string.laundry),
                                    it.tier2.laundry
                                )
                            )
                        if (it.tier2.study != 100)
                            ratingScoreList!!.add(
                                BuyerFeedbackModel(
                                    getString(R.string.study_office),
                                    it.tier2.study
                                )
                            )
                        if (it.tier2.storage != 100)
                            ratingScoreList!!.add(
                                BuyerFeedbackModel(
                                    getString(R.string.storage),
                                    it.tier2.storage
                                )
                            )
                        if (it.tier2.parking != 100)
                            ratingScoreList!!.add(
                                BuyerFeedbackModel(
                                    getString(R.string.parking_garage_carport),
                                    it.tier2.parking
                                )
                            )
                        if (it.tier2.patio != 100)
                            ratingScoreList!!.add(
                                BuyerFeedbackModel(
                                    getString(R.string.patio_deck),
                                    it.tier2.patio
                                )
                            )
                        if (it.tier2.externalAreas != 100)
                            ratingScoreList!!.add(
                                BuyerFeedbackModel(
                                    getString(R.string.external_areas_garden),
                                    it.tier2.externalAreas
                                )
                            )

                        if (ratingScoreList!!.isNotEmpty()) {
                            binding.nsvTier2.visibility = View.VISIBLE
                            binding.ivNoDataT2.visibility = View.GONE
                            val buyerFeedbackAdapter = BuyerFeedbackAdapter(this, ratingScoreList!!)
                            binding.rvTier2.apply {
                                this.adapter = buyerFeedbackAdapter
                            }
                        } else {
                            binding.nsvTier2.visibility = View.GONE
                            binding.ivNoDataT2.visibility = View.VISIBLE
                        }
                    }
                } else if (tab == 4) {
                    if (it.tier3 != null) {
                        val likeCount = it.tier3.likeCount
                        val disLikeCount = it.tier3.disLikeCount
                        binding.tvLikeCounts.text = "$likeCount / ${it.tier3.likePercent}%"
                        binding.tvDisLikeCounts.text =
                            "$disLikeCount / ${it.tier3.disLikePercent}%"

                        val likeDislikeCount = likeCount + disLikeCount

                        val likePercentage = likeCount * 100 / 1.coerceAtLeast(likeDislikeCount)
                        //                    val disLikePercentage = disLikeCount * 100 / 1.coerceAtLeast(likeDislikeCount)

                        //Display progress
                        binding.pbFeatures.progress = likePercentage

                        featuresList = ArrayList()

                        //                  Tier 3 set categories
                        it.tier3.tier3Data.let { tierData ->
                            if (tierData != null) {
                                for (i in tierData.indices) {
                                    val data = RatingDataResponse.Tier3Data()
                                    data.categoryId = tierData[i].categoryId
                                    data.category = tierData[i].category
                                    val catList: ArrayList<RatingDataResponse.Category> =
                                        ArrayList()

                                    for (j in tierData[i].categoryList!!.indices) {
                                        val cat = RatingDataResponse.Category()
                                        cat.featureId = tierData[i].categoryList!![j].featureId
                                        cat.featureName =
                                            tierData[i].categoryList!![j].featureName
                                        cat.visible = tierData[i].categoryList!![j].visible
                                        cat.like = tierData[i].categoryList!![j].like
                                        catList.add(cat)
                                    }

                                    data.expanded = false
                                    data.categoryList = catList
                                    featuresList!!.add(data)
                                }

                                if (featuresList!!.isNotEmpty()) {
                                    val featuresParentAdapter =
                                        FeedbackFeatureAdapter(this, featuresList!!)
                                    binding.rvTier3.apply {
                                        this.adapter = featuresParentAdapter
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onFeedbackClicked() {
        tab = 1
        binding.tvBuyerFeedback.typeface = boldTypeface
        binding.tvTier1.typeface = regularTypeface
        binding.tvTier2.typeface = regularTypeface
        binding.tvTier3.typeface = regularTypeface

        binding.tvBuyerFeedback.setBackgroundResource(R.drawable.bg_app_theme_button)
        binding.tvBuyerFeedback.setTextColor(getColor(R.color.grey_600))
        binding.tvTier1.setTextColor(getColor(R.color.grey_600))
        binding.tvTier1.setBackgroundResource(0)
        binding.tvTier2.setTextColor(getColor(R.color.grey_600))
        binding.tvTier2.setBackgroundResource(0)
        binding.tvTier3.setTextColor(getColor(R.color.grey_600))
        binding.tvTier3.setBackgroundResource(0)

        hideAllViews()
        binding.clFeedback.visibility = View.VISIBLE
        setData()
    }

    override fun onTier1Clicked() {
        tab = 2
        binding.tvBuyerFeedback.typeface = regularTypeface
        binding.tvTier1.typeface = boldTypeface
        binding.tvTier2.typeface = regularTypeface
        binding.tvTier3.typeface = regularTypeface

        binding.tvBuyerFeedback.setTextColor(getColor(R.color.grey_600))
        binding.tvBuyerFeedback.setBackgroundResource(0)
        binding.tvTier1.setBackgroundResource(R.drawable.bg_app_theme_button)
        binding.tvTier1.setTextColor(getColor(R.color.grey_600))
        binding.tvTier2.setTextColor(getColor(R.color.grey_600))
        binding.tvTier2.setBackgroundResource(0)
        binding.tvTier3.setTextColor(getColor(R.color.grey_600))
        binding.tvTier3.setBackgroundResource(0)

        hideAllViews()
        binding.nsvTier1.visibility = View.VISIBLE
        setData()
    }

    override fun onTier2Clicked() {
        tab = 3
        binding.tvBuyerFeedback.typeface = regularTypeface
        binding.tvTier1.typeface = regularTypeface
        binding.tvTier2.typeface = boldTypeface
        binding.tvTier3.typeface = regularTypeface

        binding.tvBuyerFeedback.setTextColor(getColor(R.color.grey_600))
        binding.tvBuyerFeedback.setBackgroundResource(0)
        binding.tvTier1.setTextColor(getColor(R.color.grey_600))
        binding.tvTier1.setBackgroundResource(0)
        binding.tvTier2.setBackgroundResource(R.drawable.bg_app_theme_button)
        binding.tvTier2.setTextColor(getColor(R.color.grey_600))
        binding.tvTier3.setTextColor(getColor(R.color.grey_600))
        binding.tvTier3.setBackgroundResource(0)

        hideAllViews()
        binding.nsvTier2.visibility = View.VISIBLE
        setData()
    }

    override fun onTier3Clicked() {
        tab = 4
        binding.tvBuyerFeedback.typeface = regularTypeface
        binding.tvTier1.typeface = regularTypeface
        binding.tvTier2.typeface = regularTypeface
        binding.tvTier3.typeface = boldTypeface

        binding.tvBuyerFeedback.setTextColor(getColor(R.color.grey_600))
        binding.tvBuyerFeedback.setBackgroundResource(0)
        binding.tvTier1.setTextColor(getColor(R.color.grey_600))
        binding.tvTier1.setBackgroundResource(0)
        binding.tvTier2.setTextColor(getColor(R.color.grey_600))
        binding.tvTier2.setBackgroundResource(0)
        binding.tvTier3.setBackgroundResource(R.drawable.bg_app_theme_button)
        binding.tvTier3.setTextColor(getColor(R.color.grey_600))

        hideAllViews()
        binding.nsvTier3.visibility = View.VISIBLE
        setData()
    }

    override fun setHeader() {
        binding.headerBar.tvTitle.text = getString(R.string.property_feedback)
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

    override fun setMessageComingFromServer(message: String?) {
        AlertDialogUtils.showSnakeBar(message, snackBar, this)
    }

    private fun init() {
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)

        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this@BuyerFeedbackActivity
            it.buyerFeedbackVM = buyerFeedbackVM
        }
        buyerFeedbackVM.attachContext(this)
        buyerFeedbackVM.setNavigator(this)
        snackBar = findViewById(android.R.id.content)
    }

    private fun initFont() {
        boldTypeface = Typeface.createFromAsset(resources.assets, "fonts/product_sans_bold.ttf")
        regularTypeface =
            Typeface.createFromAsset(resources.assets, "fonts/product_sans_regular.ttf")
        binding.tvBuyerFeedback.typeface = boldTypeface
        binding.tvTier1.typeface = regularTypeface
        binding.tvTier2.typeface = regularTypeface
        binding.tvTier3.typeface = regularTypeface
    }


    override val bindingVariable: Int
        get() = BR.buyerFeedbackVM
    override val layoutId: Int
        get() = R.layout.activity_buyer_feedback
    override val viewModel: BuyerFeedbackVM
        get() {
            buyerFeedbackVM = ViewModelProvider(this).get(BuyerFeedbackVM::class.java)
            return buyerFeedbackVM
        }

    override fun setObservers() {

    }

    private fun hideAllViews() {
        binding.clFeedback.visibility = View.GONE
        binding.nsvTier1.visibility = View.GONE
        binding.nsvTier2.visibility = View.GONE
        binding.ivNoDataT2.visibility = View.GONE
        binding.nsvTier3.visibility = View.GONE
        binding.ivNoData.visibility = View.GONE
    }

    //To active the Yes or No button
    private fun enableButton(textView: RegularTextView) {
        textView.setTextColor(this.getColor(R.color.white))
        textView.background = ContextCompat.getDrawable(this, R.drawable.bg_green_solid_curved_10)
    }

    //To de-active the Yes or No button
    private fun disableButton(textView: RegularTextView) {
        textView.setTextColor(this.getColor(R.color.grey_400))
        textView.background = ContextCompat.getDrawable(this, R.drawable.box_grey_outline_curved_10)
    }
}