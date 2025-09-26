package com.rentalhomes.ui.buyer.propertydetail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.Keys.DESCRIPTION
import com.rentalhomes.data.network.model.requestModel.SetTier3DataRequest
import com.rentalhomes.data.network.model.responseModel.GetNonListedResponse
import com.rentalhomes.data.network.model.responseModel.GetPropertyListBuyerResponse
import com.rentalhomes.data.network.model.responseModel.GetTier3
import com.rentalhomes.databinding.ActivityBuyerPropertyDetailBinding
import com.rentalhomes.databinding.DialogPropareTemplateBinding
import com.rentalhomes.ui.agent.profile.ProfileActivity
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.ui.buyer.createtemplate.CreateTemplateActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods
import com.rentalhomes.utils.KeyboardUtils
import com.rentalhomes.utils.customviews.RegularTextView
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale


class BuyerPropertyDetailActivity :
    BaseActivity<ActivityBuyerPropertyDetailBinding, BuyerPropertyDetailVM>(),
    BuyerPropertyDetailNavigator, View.OnClickListener {
    private lateinit var binding: ActivityBuyerPropertyDetailBinding
    private lateinit var buyerPropertyDetailVM: BuyerPropertyDetailVM
    private lateinit var snackBar: View
    lateinit var propertyList: GetPropertyListBuyerResponse.Data

    private var ratingScoreList: ArrayList<RatingScoreModel> = ArrayList()
    private lateinit var ratingScoreAdapter: RatingScoreAdapter

    private var featuresList: ArrayList<GetTier3.Data> = ArrayList()
    private var getTier3Data: GetTier3? = null

    //    private var featuresChildList: ArrayList<FeaturesModel.FeaturesChild> = ArrayList()
//    private var childList2: ArrayList<FeaturesModel.FeaturesChild> = ArrayList()
//    private var childList3: ArrayList<FeaturesModel.FeaturesChild> = ArrayList()
//    private var childList4: ArrayList<FeaturesModel.FeaturesChild> = ArrayList()
    private lateinit var featuresParentAdapter: FeaturesParentAdapter

    private var propertyId: Int = 0
    private var likeCount: Int = 0
    private var likepercentage: Int = 0
    private var totalFeatures: Int = 20
    private var disLikeCount: Int = 0
    private var disLikePercentage: Int = 0
    private var commonCount: Int = 0
    private lateinit var boldTypeface: Typeface
    private lateinit var regularTypeface: Typeface

    //    setTie1Details variables
    private var marketValue: Int = 0
    private var myValuation: Int = 0
    private var que1: Int = 0
    private var que2: Int = 0
    private var que3: Int = 0
    private var que3Feed: String = ""
    private var que4: Int = 0
    private var blEmail: Boolean = false
    private var blPhone: Boolean = false
    private var blMessenger: Boolean = false
    private var myNotes: String = ""

    private var location = 7
    private var streetAppeal = 7
    private var internalLayout = 7
    private var externalLayout = 7
    private var qualityOfBuilding = 7
    private var averageScore = 0
    private var que4List: ArrayList<String> = ArrayList()
    lateinit var nonListedList: GetNonListedResponse.Data
    private var defaultTemplate: Int = 0
    private var isCustomTemplateCreated: Int = 0
    private lateinit var propertyAddress: String
    private var agentId: Int = 0
    var type: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setOnListener()
        initFont()
        setData()
//        setRecyclerViewTier2()
//        setRatingScoreList()
//        setFeaturesModel()
//        setFeaturesRecyclerViewTier3()

        binding.layoutTier1.etMarketValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                binding.layoutTier1.etMarketValue.removeTextChangedListener(this)

                try {
                    var originalString: String = s.toString()
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",".toRegex(), "")
                    }
                    val longVal: Long = originalString.toLong()
                    val formatter: DecimalFormat =
                        NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString: String = formatter.format(longVal)

                    //setting text after format to EditText
                    binding.layoutTier1.etMarketValue.setText(formattedString)
                    binding.layoutTier1.etMarketValue.setSelection(binding.layoutTier1.etMarketValue.text!!.length)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                binding.layoutTier1.etMarketValue.addTextChangedListener(this)
            }
        })

        binding.layoutTier1.etMyValuation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                binding.layoutTier1.etMyValuation.removeTextChangedListener(this)

                try {
                    var originalString: String = s.toString()
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",".toRegex(), "")
                    }
                    val longVal: Long = originalString.toLong()
                    val formatter: DecimalFormat =
                        NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString: String = formatter.format(longVal)

                    //setting text after format to EditText
                    binding.layoutTier1.etMyValuation.setText(formattedString)
                    binding.layoutTier1.etMyValuation.setSelection(binding.layoutTier1.etMyValuation.text!!.length)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                binding.layoutTier1.etMyValuation.addTextChangedListener(this)
            }
        })
    }

    private fun setData() {

        type = intent.extras?.getString(Keys.TYPE)!!
        if (type == "1") {
            nonListedList = intent.getParcelableExtra(Keys.NON_LISTED)!!
            propertyId = nonListedList.propertyId
            buyerPropertyDetailVM.propertyId.value = propertyId
            if (nonListedList.propertyImage!!.isNotEmpty()) {
                Glide.with(this).load(nonListedList.propertyImage).into(binding.ivProperty)
            }
            binding.tvAddress.text = nonListedList.propertyAddress.toString()
            binding.tvCity.text = nonListedList.propertyCity.toString()
            binding.tvBed.text = nonListedList.bed.toString()
            binding.tvBath.text = nonListedList.bath.toString()
            binding.tvCar.text = nonListedList.car.toString()
            binding.tvLandMeters.text =
                "${nonListedList.landSize.toString()} ${getString(R.string.meters)}"

            Glide.with(this).load(nonListedList.propertyQRImage).into(binding.ivQrCode)
            buyerPropertyDetailVM.mldTemplateType.value = nonListedList.defaultTemplateType
            binding.btnCompare.visibility = View.GONE
            binding.clBottom.visibility = View.GONE
            binding.bottomView.visibility = View.GONE
            binding.tvSaleStatus.visibility = View.GONE

            if (!TextUtils.isEmpty(nonListedList.description.toString())) {
                binding.tvTitleDesc.visibility = View.VISIBLE
                binding.tvDescription.visibility = View.VISIBLE
                binding.tvDescription.text = nonListedList.description.toString()
            }

            if (TextUtils.isEmpty(nonListedList.propertyQRImage.toString())) {
                binding.tvTapToOpenQR.visibility = View.INVISIBLE
            }

        } else {

            propertyList = intent.getParcelableExtra("BUYER_LIST")!!

            propertyId = propertyList.propertyId!!
            propertyAddress = propertyList.propertyAddress!!
            buyerPropertyDetailVM.propertyId.value = propertyId
            agentId = propertyList.agentDetails?.agentId!!

            if (propertyList.propertyType == 1) {
                binding.bottomView.visibility = View.GONE
                binding.clBottom.visibility = View.GONE
            } else {
                binding.clBottom.visibility = View.VISIBLE
                binding.bottomView.visibility = View.VISIBLE

                binding.tvName.text = propertyList.agentDetails?.agentName.toString()
                Glide.with(this).load(propertyList.agentDetails?.agentProfilePic)
                    .placeholder(R.drawable.ic_profile_place_holder).into(binding.ivProfile)
                binding.tvMobile.text = "+61 ${propertyList.agentDetails?.agentMobile.toString()}"

                binding.tvName.setOnClickListener { binding.ivProfile.performClick() }

                binding.ivProfile.setOnClickListener {
                    if (propertyList.agentDetails!!.agentId != 0) {
                        val intent = Intent(this, ProfileActivity::class.java)
                        intent.putExtra(Keys.IS_OTHER_USER, true)
                        intent.putExtra(Keys.OTHER_USER_ID, propertyList.agentDetails!!.agentId)
                        startActivity(intent)
                    }
                }
            }

            if (propertyList.propertyImage!!.isNotEmpty()) {
                Glide.with(this).load(propertyList.propertyImage).into(binding.ivProperty)
            }
            binding.tvAddress.text = propertyList.propertyAddress.toString()
            binding.tvCity.text = propertyList.propertyCity.toString()
            binding.tvBed.text = propertyList.bed.toString()
            binding.tvBath.text = propertyList.bath.toString()
            binding.tvCar.text = propertyList.car.toString()
            binding.tvLandMeters.text =
                "${propertyList.landSize.toString()} ${getString(R.string.meters)}"
            binding.tvSaleStatus.text = propertyList.propertyStatus.toString()
            Glide.with(this).load(propertyList.propertyQRImage).into(binding.ivQrCode)

            buyerPropertyDetailVM.mldTemplateType.value = propertyList.defaultTemplateType
            binding.btnCompare.visibility = View.VISIBLE
            binding.tvSaleStatus.visibility = View.VISIBLE

            if (!TextUtils.isEmpty(propertyList.description.toString())) {
                binding.tvTitleDesc.visibility = View.VISIBLE
                binding.tvDescription.visibility = View.VISIBLE
                binding.tvDescription.text = propertyList.description.toString()

                val tvDec = binding.tvDescription.viewTreeObserver
                tvDec.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val obs = binding.tvDescription.viewTreeObserver
                        obs.removeOnGlobalLayoutListener(this)
                        Log.e("lines", "count: " + binding.tvDescription.lineCount)

                        if (binding.tvDescription.lineCount > 3) {
                            binding.tvDescription.maxLines = 3
                            binding.tvViewMore.visibility = View.VISIBLE
                        } else {
                            binding.tvViewMore.visibility = View.GONE
                        }
                    }
                })
            }

            if (TextUtils.isEmpty(propertyList.propertyQRImage.toString())) {
                binding.tvTapToOpenQR.visibility = View.INVISIBLE
            }
        }
    }

    fun makeTextViewResizable(tv: TextView, maxLine: Int, expandText: String, viewMore: Boolean) {
        if (tv.tag == null) {
            tv.tag = tv.text
        }
        val vto = tv.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val text: String
                val lineEndIndex: Int
                val obs = tv.viewTreeObserver
                obs.removeOnGlobalLayoutListener(this)
                Log.e("lines", "count: " + tv.lineCount)
                if (maxLine == 0) {
                    lineEndIndex = tv.layout.getLineEnd(0)
                    text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                        .toString() + " " + expandText
                    binding.tvViewMore.visibility = View.VISIBLE
                } else if (maxLine > 0 && tv.lineCount >= maxLine) {
                    lineEndIndex = tv.layout.getLineEnd(maxLine - 1)
                    text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                        .toString() + " " + expandText
                    binding.tvViewMore.visibility = View.VISIBLE
                } else {
                    lineEndIndex = tv.layout.getLineEnd(tv.layout.lineCount - 1)
                    text = tv.text.subSequence(0, lineEndIndex).toString() + " " + expandText
                    binding.tvViewMore.visibility = View.VISIBLE
                }
                tv.text = text
                tv.movementMethod = LinkMovementMethod.getInstance()
                tv.setText(
                    addClickablePartTextViewResizable(
                        SpannableString(tv.text.toString()), tv, lineEndIndex, expandText,
                        viewMore
                    ), TextView.BufferType.SPANNABLE
                )
            }
        })
    }

    private fun addClickablePartTextViewResizable(
        strSpanned: Spanned, tv: TextView,
        maxLine: Int, spanableText: String, viewMore: Boolean
    ): SpannableStringBuilder? {
        val str = strSpanned.toString()
        val ssb = SpannableStringBuilder(strSpanned)
        if (str.contains(spanableText)) {
            ssb.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    tv.layoutParams = tv.layoutParams
                    tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
                    tv.invalidate()
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "...View Less", false)
                    } else {
                        makeTextViewResizable(tv, 3, "...View More", true)
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length, 0)
        }
        return ssb
    }

    /*private fun addViewMoreToTextView(
        textView: RegularTextView,
        text: String,
        expandText: String,
        maxLine: Int
    ) {
        try {
            textView.post {
                val truncatedSpannableString: SpannableStringBuilder
                val startIndex: Int
                if (textView.lineCount > maxLine) {
                    val lastCharShown = textView.layout.getLineVisibleEnd(maxLine - 1)
                    val displayText =
                        text.substring(0, lastCharShown - expandText.length + 1) + " " + expandText
                    startIndex = displayText.indexOf(expandText)
                    truncatedSpannableString = SpannableStringBuilder(displayText)
                    textView.text = truncatedSpannableString
                    truncatedSpannableString.setSpan(
                        object : ClickableSpan() {
                            override fun onClick(widget: View) {
                                // this click event is not firing that's why we are adding click event for text view below.
                            }
                        },
                        startIndex,
                        startIndex + expandText.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    textView.text = truncatedSpannableString
                    textView.setOnClickListener {
                        //TODO View more click
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/

    /*private fun addClickablePartTextResizable(
         strSpanned: String,
         tv: TextView,
         maxLine: Int,
         spannableText: String?,
         viewMore: Boolean
     ): Spannable {
         val ssb = SpannableStringBuilder(strSpanned)
         if (strSpanned.contains(spannableText!!)) {
             if (strSpanned.contains(spannableText)) {
                 ssb.setSpan(
                     object : ClickableSpan() {
                         override fun onClick(widget: View) {
                             if (viewMore) {
                                 tv.layoutParams = tv.layoutParams
                                 tv.setText(
                                     tv.tag.toString(),
                                     BufferType.SPANNABLE
                                 )
                                 tv.invalidate()
                                 makeTextViewResizable(
                                     tv, -3, "...View Less",
                                     false
                                 )
                                 tv.setTextColor(Color.BLACK)
                             } else {
                                 tv.layoutParams = tv.layoutParams
                                 tv.setText(
                                     tv.tag.toString(),
                                     BufferType.SPANNABLE
                                 )
                                 tv.invalidate()
                                 makeTextViewResizable(
                                     tv, 3, "...View More",
                                     true
                                 )
                                 tv.setTextColor(Color.BLACK)
                             }
                         }
                     }, strSpanned.indexOf(spannableText),
                     strSpanned.indexOf(spannableText) + spannableText.length, 0
                 )
             }
         }
         return ssb
     }

     fun makeTextViewResizable(
        tv: TextView,
        maxLine: Int, expandText: String, viewMore: Boolean
    ) {
        if (tv.tag == null) {
            tv.tag = tv.text
        }
        val vto = tv.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val obs = tv.viewTreeObserver
                obs.removeGlobalOnLayoutListener(this)
                if (maxLine == 0) {
                    val lineEndIndex = tv.layout.getLineEnd(0)
                    val text = tv.text.subSequence(
                        0,
                        lineEndIndex - expandText.length + 1
                    )
                        .toString() + " " + expandText
                    tv.text = text
                    tv.movementMethod = LinkMovementMethod.getInstance()
                    tv.setText(
                        addClickablePartTextResizable(
                            tv.text
                                .toString(), tv, maxLine, expandText,
                            viewMore
                        ), TextView.BufferType.SPANNABLE
                    )
                } else if (maxLine > 0 && tv.lineCount >= maxLine) {
                    val lineEndIndex = tv.layout.getLineEnd(maxLine - 1)
                    val text = tv.text.subSequence(
                        0,
                        lineEndIndex - expandText.length + 1
                    )
                        .toString() + " " + expandText
                    tv.text = text
                    tv.movementMethod = LinkMovementMethod.getInstance()
                    tv.setText(
                        addClickablePartTextResizable(
                            tv.text
                                .toString(), tv, maxLine, expandText,
                            viewMore
                        ), TextView.BufferType.SPANNABLE
                    )
                } else {
                    val lineEndIndex = tv.layout.getLineEnd(
                        tv.layout.lineCount - 1
                    )
                    val text = tv.text.subSequence(0, lineEndIndex)
                        .toString() + " " + expandText
                    tv.text = text
                    tv.movementMethod = LinkMovementMethod.getInstance()
                    tv.setText(
                        addClickablePartTextResizable(
                            tv.text
                                .toString(), tv, lineEndIndex, expandText,
                            viewMore
                        ), TextView.BufferType.SPANNABLE
                    )
                }
            }
        })
    }*/

    private fun initFont() {
        boldTypeface = Typeface.createFromAsset(resources.assets, "fonts/product_sans_bold.ttf")
        regularTypeface =
            Typeface.createFromAsset(resources.assets, "fonts/product_sans_regular.ttf")
        binding.tvPropertyDetail.typeface = boldTypeface
        binding.tvTier1.typeface = regularTypeface
        binding.tvTier2.typeface = regularTypeface
        binding.tvTier3.typeface = regularTypeface
    }

    private fun init() {
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)

        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this@BuyerPropertyDetailActivity
            it.buyerPropertyDetailVM = buyerPropertyDetailVM
        }
        buyerPropertyDetailVM.attachContext(this)
        buyerPropertyDetailVM.setNavigator(this)
        snackBar = findViewById(android.R.id.content)
    }

    override val bindingVariable: Int
        get() = BR.buyerPropertyDetailVM
    override val layoutId: Int
        get() = R.layout.activity_buyer_property_detail
    override val viewModel: BuyerPropertyDetailVM
        get() {
            buyerPropertyDetailVM = ViewModelProvider(this).get(BuyerPropertyDetailVM::class.java)
            return buyerPropertyDetailVM
        }

    override fun setHeader() {
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnListener() {
//        binding.layoutTier1.tvDetails.setOnClickListener(this)
//        binding.layoutTier1.tvScoring.setOnClickListener(this)
//        binding.layoutTier1.etMarketValue.setOnClickListener {
//            binding.layoutTier1.etMarketValue.focusable = View.FOCUSABLE
//            binding.layoutTier1.etMarketValue.requestFocus()
//        }
//        binding.layoutTier1.etMyValuation.setOnClickListener {
//            binding.layoutTier1.etMyValuation.focusable = View.FOCUSABLE
//            binding.layoutTier1.etMyValuation.requestFocus()
//        }
        //Location rating score
        binding.layoutTier1.tvZeroLocation.setOnClickListener(this)
        binding.layoutTier1.tvLowLocation.setOnClickListener(this)
        binding.layoutTier1.tvMeetsLocation.setOnClickListener(this)
        binding.layoutTier1.tvHigherLocation.setOnClickListener(this)
        binding.layoutTier1.tvExceedsLocation.setOnClickListener(this)

        //Street Appeal rating score
        binding.layoutTier1.tvZeroStreetAppeal.setOnClickListener(this)
        binding.layoutTier1.tvLowStreetAppeal.setOnClickListener(this)
        binding.layoutTier1.tvMeetsStreetAppeal.setOnClickListener(this)
        binding.layoutTier1.tvHigherStreetAppeal.setOnClickListener(this)
        binding.layoutTier1.tvExceedsStreetAppeal.setOnClickListener(this)

        //Internal Layout rating score
        binding.layoutTier1.tvZeroInternalLayout.setOnClickListener(this)
        binding.layoutTier1.tvLowInternalLayout.setOnClickListener(this)
        binding.layoutTier1.tvMeetsInternalLayout.setOnClickListener(this)
        binding.layoutTier1.tvHigherInternalLayout.setOnClickListener(this)
        binding.layoutTier1.tvExceedsInternalLayout.setOnClickListener(this)

        //External Layout rating score
        binding.layoutTier1.tvZeroExternalLayout.setOnClickListener(this)
        binding.layoutTier1.tvLowExternalLayout.setOnClickListener(this)
        binding.layoutTier1.tvMeetsExternalLayout.setOnClickListener(this)
        binding.layoutTier1.tvHigherExternalLayout.setOnClickListener(this)
        binding.layoutTier1.tvExceedsExternalLayout.setOnClickListener(this)

        //Quality of Building rating score
        binding.layoutTier1.tvZeroQualityBuilding.setOnClickListener(this)
        binding.layoutTier1.tvLowQualityBuilding.setOnClickListener(this)
        binding.layoutTier1.tvMeetsQualityBuilding.setOnClickListener(this)
        binding.layoutTier1.tvHigherQualityBuilding.setOnClickListener(this)
        binding.layoutTier1.tvExceedsQualityBuilding.setOnClickListener(this)

        //Question 1 yes no
        binding.layoutQuestions.tvQuestion1Yes.setOnClickListener(this)
        binding.layoutQuestions.tvQuestion1No.setOnClickListener(this)

        //Question 2 yes no
        binding.layoutQuestions.tvQuestion2Yes.setOnClickListener(this)
        binding.layoutQuestions.tvQuestion2No.setOnClickListener(this)

        //Question 3 yes no
        binding.layoutQuestions.tvQuestion3Yes.setOnClickListener(this)
        binding.layoutQuestions.tvQuestion3No.setOnClickListener(this)

        //Question 4
        binding.layoutQuestions.tvQuestion4Email.setOnClickListener(this)
        binding.layoutQuestions.tvQuestion4Phone.setOnClickListener(this)
        binding.layoutQuestions.tvQuestion4PropareMsg.setOnClickListener(this)

        //Tier 1 save & cancel
        binding.layoutQuestions.btnSaveQue.setOnClickListener(this)
        binding.layoutTier1.btnTier1RatingSave.setOnClickListener(this)

        //Tier 3 Create Template
        binding.clCreateTemplate.setOnClickListener(this)
        binding.tvPropareTemplate.setOnClickListener(this)
        binding.btnSaveTier3.setOnClickListener(this)

        //Hides keyboard when scrolling
        binding.nsv.setOnTouchListener { v, event ->
            KeyboardUtils.hideKeyboard(this, snackBar)
            v?.onTouchEvent(event) ?: true
        }

        binding.layoutTier1.ivTier1Info.setOnClickListener {
            GlobalMethods.getTooltipTop(
                this,
                binding.layoutTier1.ivTier1Info,
                getString(R.string.lorem_ipsum_short)
            )
        }

        binding.layoutTier2.ivTier2Info.setOnClickListener {
            GlobalMethods.getTooltipTop(
                this,
                binding.layoutTier2.ivTier2Info,
                getString(R.string.lorem_ipsum_short)
            )
        }

        binding.ivTier3Info.setOnClickListener {
            GlobalMethods.getTooltipTop(
                this,
                binding.ivTier3Info,
                getString(R.string.lorem_ipsum_short)
            )
        }



        binding.tvViewMore.setOnClickListener {
            var intent = Intent(this@BuyerPropertyDetailActivity, DescriptionActivity::class.java)
            intent.putExtra(DESCRIPTION, propertyList.description)
            startActivity(intent)
        }

    }

    @SuppressLint("SetTextI18n")
    override fun setObservers() {

    }

    //Back button click
    override fun onBackPress() {
        onBackPressed()
    }

    //Tier tabs clicks
    override fun onPropertyDetailClicked() {
        binding.tvPropertyDetail.typeface = boldTypeface
        binding.tvQue.typeface = regularTypeface
        binding.tvTier1.typeface = regularTypeface
        binding.tvTier2.typeface = regularTypeface
        binding.tvTier3.typeface = regularTypeface

        binding.tvPropertyDetail.setBackgroundResource(R.drawable.bg_app_theme_button)
        binding.tvPropertyDetail.setTextColor(getColor(R.color.grey_600))
        binding.tvQue.setTextColor(getColor(R.color.grey_600))
        binding.tvQue.setBackgroundResource(0)
        binding.tvTier1.setTextColor(getColor(R.color.grey_600))
        binding.tvTier1.setBackgroundResource(0)
        binding.tvTier2.setTextColor(getColor(R.color.grey_600))
        binding.tvTier2.setBackgroundResource(0)
        binding.tvTier3.setTextColor(getColor(R.color.grey_600))
        binding.tvTier3.setBackgroundResource(0)

        binding.clPropertyDetail.visibility = View.VISIBLE
        binding.layoutQuestions.clQuestions.visibility = View.GONE
        binding.layoutTier1.clTier1.visibility = View.GONE
        binding.layoutTier2.clTier2.visibility = View.GONE
        binding.clTier3.visibility = View.GONE

//        binding.nsv.fullScroll(View.FOCUS_DOWN)
        binding.nsv.fullScroll(View.FOCUS_UP)
        binding.nsv.scrollTo(0, 0)
    }

    override fun onQuestionClicked() {
//        Setting the font to bold on click
        binding.tvPropertyDetail.typeface = regularTypeface
        binding.tvQue.typeface = boldTypeface
        binding.tvTier1.typeface = regularTypeface
        binding.tvTier2.typeface = regularTypeface
        binding.tvTier3.typeface = regularTypeface

        binding.tvQue.setBackgroundResource(R.drawable.bg_app_theme_button)
        binding.tvQue.setTextColor(getColor(R.color.grey_600))
        binding.tvPropertyDetail.setTextColor(getColor(R.color.grey_600))
        binding.tvPropertyDetail.setBackgroundResource(0)
        binding.tvTier1.setTextColor(getColor(R.color.grey_600))
        binding.tvTier1.setBackgroundResource(0)
        binding.tvTier2.setTextColor(getColor(R.color.grey_600))
        binding.tvTier2.setBackgroundResource(0)
        binding.tvTier3.setTextColor(getColor(R.color.grey_600))
        binding.tvTier3.setBackgroundResource(0)

        binding.clPropertyDetail.visibility = View.GONE
        binding.layoutQuestions.clQuestions.visibility = View.VISIBLE
        binding.layoutTier1.clTier1.visibility = View.GONE
        binding.layoutTier2.clTier2.visibility = View.GONE
        binding.clTier3.visibility = View.GONE

        binding.nsv.fullScroll(View.FOCUS_UP)
        binding.nsv.scrollTo(0, 0)

        getTier1Details(propertyId)
    }

    override fun onTier1Clicked() {
//        Setting the font to bold on click
        binding.tvPropertyDetail.typeface = regularTypeface
        binding.tvQue.typeface = regularTypeface
        binding.tvTier1.typeface = boldTypeface
        binding.tvTier2.typeface = regularTypeface
        binding.tvTier3.typeface = regularTypeface

        binding.tvTier1.setBackgroundResource(R.drawable.bg_app_theme_button)
        binding.tvTier1.setTextColor(getColor(R.color.grey_600))
        binding.tvPropertyDetail.setTextColor(getColor(R.color.grey_600))
        binding.tvPropertyDetail.setBackgroundResource(0)
        binding.tvQue.setTextColor(getColor(R.color.grey_600))
        binding.tvQue.setBackgroundResource(0)
        binding.tvTier2.setTextColor(getColor(R.color.grey_600))
        binding.tvTier2.setBackgroundResource(0)
        binding.tvTier3.setTextColor(getColor(R.color.grey_600))
        binding.tvTier3.setBackgroundResource(0)

        binding.clPropertyDetail.visibility = View.GONE
        binding.layoutQuestions.clQuestions.visibility = View.GONE
        binding.layoutTier1.clTier1.visibility = View.VISIBLE
        binding.layoutTier2.clTier2.visibility = View.GONE
        binding.clTier3.visibility = View.GONE

        binding.nsv.fullScroll(View.FOCUS_UP)
        binding.nsv.scrollTo(0, 0)

        getTier1Details(propertyId)
    }

    override fun onTier2Clicked() {
//        Setting the font to bold on click
        binding.tvPropertyDetail.typeface = regularTypeface
        binding.tvQue.typeface = regularTypeface
        binding.tvTier1.typeface = regularTypeface
        binding.tvTier2.typeface = boldTypeface
        binding.tvTier3.typeface = regularTypeface

        binding.tvTier2.setBackgroundResource(R.drawable.bg_app_theme_button)
        binding.tvTier2.setTextColor(getColor(R.color.grey_600))
        binding.tvPropertyDetail.setTextColor(getColor(R.color.grey_600))
        binding.tvPropertyDetail.setBackgroundResource(0)
        binding.tvQue.setTextColor(getColor(R.color.grey_600))
        binding.tvQue.setBackgroundResource(0)
        binding.tvTier1.setTextColor(getColor(R.color.grey_600))
        binding.tvTier1.setBackgroundResource(0)
        binding.tvTier3.setTextColor(getColor(R.color.grey_600))
        binding.tvTier3.setBackgroundResource(0)

        binding.clPropertyDetail.visibility = View.GONE
        binding.layoutQuestions.clQuestions.visibility = View.GONE
        binding.layoutTier1.clTier1.visibility = View.GONE
        binding.layoutTier2.clTier2.visibility = View.VISIBLE
        binding.clTier3.visibility = View.GONE

//        binding.nsv.fullScroll(View.FOCUS_UP)
        binding.nsv.scrollTo(0, 0)

        getTier2Details(propertyId)
    }

    override fun onTier3Clicked() {
//        Setting the font to bold on click
        binding.tvPropertyDetail.typeface = regularTypeface
        binding.tvQue.typeface = regularTypeface
        binding.tvTier1.typeface = regularTypeface
        binding.tvTier2.typeface = regularTypeface
        binding.tvTier3.typeface = boldTypeface

        binding.tvTier3.setBackgroundResource(R.drawable.bg_app_theme_button)
        binding.tvTier3.setTextColor(getColor(R.color.grey_600))
        binding.tvPropertyDetail.setTextColor(getColor(R.color.grey_600))
        binding.tvPropertyDetail.setBackgroundResource(0)
        binding.tvQue.setTextColor(getColor(R.color.grey_600))
        binding.tvQue.setBackgroundResource(0)
        binding.tvTier1.setTextColor(getColor(R.color.grey_600))
        binding.tvTier1.setBackgroundResource(0)
        binding.tvTier2.setTextColor(getColor(R.color.grey_600))
        binding.tvTier2.setBackgroundResource(0)

        binding.clPropertyDetail.visibility = View.GONE
        binding.layoutQuestions.clQuestions.visibility = View.GONE
        binding.layoutTier1.clTier1.visibility = View.GONE
        binding.layoutTier2.clTier2.visibility = View.GONE
        binding.clTier3.visibility = View.VISIBLE

        binding.nsv.fullScroll(View.FOCUS_UP)
        binding.nsv.scrollTo(0, 0)

//        Tier 3 API call
        callTier3Data()
    }

    override fun onResume() {
        super.onResume()
        if (binding.clTier3.visibility == View.VISIBLE)
            callTier3Data()
    }

    //        Tier 3 API call
    @SuppressLint("SetTextI18n")
    private fun callTier3Data() {
        buyerPropertyDetailVM.getTier3Data(propertyId)

//        Tier 3 data observer
        buyerPropertyDetailVM.tier3Data.observe(this) { tier3 ->
            getTier3Data = tier3

            defaultTemplate = tier3.defaultTemplateType
            isCustomTemplateCreated = tier3.isCustomTemplateCreated

            if (tier3.isCustomTemplateCreated == 1) {
                binding.clCreateTemplate.visibility = View.GONE
                binding.tvPropareTemplate.visibility = View.VISIBLE
            } else {
                binding.clCreateTemplate.visibility = View.VISIBLE
                binding.tvPropareTemplate.visibility = View.GONE
            }

            if (tier3.defaultTemplateType == 1) {
                buyerPropertyDetailVM.mldTemplateType.value = 1
                binding.tvPropareTemplate.text = getString(R.string.propare_template)
            } else if (tier3.defaultTemplateType == 2) {
                buyerPropertyDetailVM.mldTemplateType.value = 2
                binding.tvPropareTemplate.text = getString(R.string.custom_template)
            }

            likeCount = tier3.likeCount
            disLikeCount = tier3.disLikeCount

            binding.tvLikeCounts.text = "$likeCount / ${tier3.likePercent}%"
            binding.tvDisLikeCounts.text = "$disLikeCount / ${tier3.disLikePercent}%"

            buyerPropertyDetailVM.likeCount.value = tier3.likeCount
            buyerPropertyDetailVM.likePercent.value = tier3.likePercent
            buyerPropertyDetailVM.disLikeCount.value = tier3.disLikeCount
            buyerPropertyDetailVM.disLikePercent.value = tier3.disLikePercent

//            if (likeCount != 0 && disLikeCount != 0) {
            val likeDislikeCount = likeCount + disLikeCount

            likepercentage = likeCount * 100 / 1.coerceAtLeast(likeDislikeCount)
            disLikePercentage = disLikeCount * 100 / 1.coerceAtLeast(likeDislikeCount)

            //Display progress
            binding.pbFeatures.progress = likepercentage
//            }

            featuresList.clear()

//          Tier 3 set categories
            tier3.data.let { tierData ->
                var like = 0
                var dislike = 0
                if (tierData != null) {

                    for (i in tierData.indices) {
                        val data = GetTier3.Data()
                        data.categoryId = tierData[i].categoryId
                        data.category = tierData[i].category
                        val catList: ArrayList<GetTier3.Category> = ArrayList()

                        for (j in tierData[i].categoryList!!.indices) {
//                            if (tierData[i].categoryList!![j].visible == 1) {
                            val cat = GetTier3.Category()
                            cat.featureId = tierData[i].categoryList!![j].featureId
                            cat.featureName = tierData[i].categoryList!![j].featureName
                            cat.visible = tierData[i].categoryList!![j].visible
                            cat.like = tierData[i].categoryList!![j].like
                            catList.add(cat)
//                            }
                        }


                        data.expanded = false
                        data.categoryList = catList
                        featuresList.add(data)
                        updateProgressBar()
                    }

                    if (featuresList.isNotEmpty()) {
                        featuresParentAdapter = FeaturesParentAdapter(this, featuresList, this, -1)
                        binding.rvFeaturesParent.apply {
                            this.adapter = featuresParentAdapter
                        }
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        v?.let { view ->
            when (view.id) {

                R.id.tvZeroLocation -> {
                    location = 0
                    setAlpha(
                        binding.layoutTier1.tvZeroLocation,
                        binding.layoutTier1.tvLowLocation,
                        binding.layoutTier1.tvMeetsLocation,
                        binding.layoutTier1.tvHigherLocation,
                        binding.layoutTier1.tvExceedsLocation
                    )
                }
                R.id.tvLowLocation -> {
                    location = 1
                    setAlpha(
                        binding.layoutTier1.tvLowLocation,
                        binding.layoutTier1.tvZeroLocation,
                        binding.layoutTier1.tvMeetsLocation,
                        binding.layoutTier1.tvHigherLocation,
                        binding.layoutTier1.tvExceedsLocation
                    )
                }
                R.id.tvMeetsLocation -> {
                    location = 3
                    setAlpha(
                        binding.layoutTier1.tvMeetsLocation,
                        binding.layoutTier1.tvZeroLocation,
                        binding.layoutTier1.tvLowLocation,
                        binding.layoutTier1.tvHigherLocation,
                        binding.layoutTier1.tvExceedsLocation
                    )
                }
                R.id.tvHigherLocation -> {
                    location = 5
                    setAlpha(
                        binding.layoutTier1.tvHigherLocation,
                        binding.layoutTier1.tvZeroLocation,
                        binding.layoutTier1.tvLowLocation,
                        binding.layoutTier1.tvMeetsLocation,
                        binding.layoutTier1.tvExceedsLocation
                    )
                }
                R.id.tvExceedsLocation -> {
                    location = 6
                    setAlpha(
                        binding.layoutTier1.tvExceedsLocation,
                        binding.layoutTier1.tvZeroLocation,
                        binding.layoutTier1.tvLowLocation,
                        binding.layoutTier1.tvMeetsLocation,
                        binding.layoutTier1.tvHigherLocation
                    )
                }

                //Street Appeal rating score
                R.id.tvZeroStreetAppeal -> {
                    streetAppeal = 0
                    setAlpha(
                        binding.layoutTier1.tvZeroStreetAppeal,
                        binding.layoutTier1.tvLowStreetAppeal,
                        binding.layoutTier1.tvMeetsStreetAppeal,
                        binding.layoutTier1.tvHigherStreetAppeal,
                        binding.layoutTier1.tvExceedsStreetAppeal
                    )
                }
                R.id.tvLowStreetAppeal -> {
                    streetAppeal = 1
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvLowStreetAppeal,
                        binding.layoutTier1.tvZeroStreetAppeal,
                        binding.layoutTier1.tvMeetsStreetAppeal,
                        binding.layoutTier1.tvHigherStreetAppeal,
                        binding.layoutTier1.tvExceedsStreetAppeal
                    )
                }
                R.id.tvMeetsStreetAppeal -> {
                    streetAppeal = 3
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvMeetsStreetAppeal,
                        binding.layoutTier1.tvZeroStreetAppeal,
                        binding.layoutTier1.tvLowStreetAppeal,
                        binding.layoutTier1.tvHigherStreetAppeal,
                        binding.layoutTier1.tvExceedsStreetAppeal
                    )
                }
                R.id.tvHigherStreetAppeal -> {
                    streetAppeal = 5
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvHigherStreetAppeal,
                        binding.layoutTier1.tvZeroStreetAppeal,
                        binding.layoutTier1.tvLowStreetAppeal,
                        binding.layoutTier1.tvMeetsStreetAppeal,
                        binding.layoutTier1.tvExceedsStreetAppeal
                    )
                }
                R.id.tvExceedsStreetAppeal -> {
                    streetAppeal = 6
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvExceedsStreetAppeal,
                        binding.layoutTier1.tvZeroStreetAppeal,
                        binding.layoutTier1.tvLowStreetAppeal,
                        binding.layoutTier1.tvMeetsStreetAppeal,
                        binding.layoutTier1.tvHigherStreetAppeal
                    )
                }

                //Internal Layout rating score
                R.id.tvZeroInternalLayout -> {
                    internalLayout = 0
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvZeroInternalLayout,
                        binding.layoutTier1.tvLowInternalLayout,
                        binding.layoutTier1.tvMeetsInternalLayout,
                        binding.layoutTier1.tvHigherInternalLayout,
                        binding.layoutTier1.tvExceedsInternalLayout
                    )
                }
                R.id.tvLowInternalLayout -> {
                    internalLayout = 1
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvLowInternalLayout,
                        binding.layoutTier1.tvZeroInternalLayout,
                        binding.layoutTier1.tvMeetsInternalLayout,
                        binding.layoutTier1.tvHigherInternalLayout,
                        binding.layoutTier1.tvExceedsInternalLayout
                    )
                }
                R.id.tvMeetsInternalLayout -> {
                    internalLayout = 3
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvMeetsInternalLayout,
                        binding.layoutTier1.tvZeroInternalLayout,
                        binding.layoutTier1.tvLowInternalLayout,
                        binding.layoutTier1.tvHigherInternalLayout,
                        binding.layoutTier1.tvExceedsInternalLayout
                    )
                }
                R.id.tvHigherInternalLayout -> {
                    internalLayout = 5
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvHigherInternalLayout,
                        binding.layoutTier1.tvZeroInternalLayout,
                        binding.layoutTier1.tvLowInternalLayout,
                        binding.layoutTier1.tvMeetsInternalLayout,
                        binding.layoutTier1.tvExceedsInternalLayout
                    )
                }
                R.id.tvExceedsInternalLayout -> {
                    internalLayout = 6
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvExceedsInternalLayout,
                        binding.layoutTier1.tvZeroInternalLayout,
                        binding.layoutTier1.tvLowInternalLayout,
                        binding.layoutTier1.tvMeetsInternalLayout,
                        binding.layoutTier1.tvHigherInternalLayout
                    )
                }

                //External Layout rating score
                R.id.tvZeroExternalLayout -> {
                    externalLayout = 0
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvZeroExternalLayout,
                        binding.layoutTier1.tvLowExternalLayout,
                        binding.layoutTier1.tvMeetsExternalLayout,
                        binding.layoutTier1.tvHigherExternalLayout,
                        binding.layoutTier1.tvExceedsExternalLayout
                    )
                }
                R.id.tvLowExternalLayout -> {
                    externalLayout = 1
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvLowExternalLayout,
                        binding.layoutTier1.tvZeroExternalLayout,
                        binding.layoutTier1.tvMeetsExternalLayout,
                        binding.layoutTier1.tvHigherExternalLayout,
                        binding.layoutTier1.tvExceedsExternalLayout
                    )
                }
                R.id.tvMeetsExternalLayout -> {
                    externalLayout = 3
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvMeetsExternalLayout,
                        binding.layoutTier1.tvZeroExternalLayout,
                        binding.layoutTier1.tvLowExternalLayout,
                        binding.layoutTier1.tvHigherExternalLayout,
                        binding.layoutTier1.tvExceedsExternalLayout
                    )
                }
                R.id.tvHigherExternalLayout -> {
                    externalLayout = 5
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvHigherExternalLayout,
                        binding.layoutTier1.tvZeroExternalLayout,
                        binding.layoutTier1.tvLowExternalLayout,
                        binding.layoutTier1.tvMeetsExternalLayout,
                        binding.layoutTier1.tvExceedsExternalLayout
                    )
                }
                R.id.tvExceedsExternalLayout -> {
                    externalLayout = 6
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvExceedsExternalLayout,
                        binding.layoutTier1.tvZeroExternalLayout,
                        binding.layoutTier1.tvLowExternalLayout,
                        binding.layoutTier1.tvMeetsExternalLayout,
                        binding.layoutTier1.tvHigherExternalLayout
                    )
                }

                //Quality of Building Layout rating score
                R.id.tvZeroQualityBuilding -> {
                    qualityOfBuilding = 0
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvZeroQualityBuilding,
                        binding.layoutTier1.tvLowQualityBuilding,
                        binding.layoutTier1.tvMeetsQualityBuilding,
                        binding.layoutTier1.tvHigherQualityBuilding,
                        binding.layoutTier1.tvExceedsQualityBuilding
                    )
                }
                R.id.tvLowQualityBuilding -> {
                    qualityOfBuilding = 1
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvLowQualityBuilding,
                        binding.layoutTier1.tvZeroQualityBuilding,
                        binding.layoutTier1.tvMeetsQualityBuilding,
                        binding.layoutTier1.tvHigherQualityBuilding,
                        binding.layoutTier1.tvExceedsQualityBuilding
                    )
                }
                R.id.tvMeetsQualityBuilding -> {
                    qualityOfBuilding = 3
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvMeetsQualityBuilding,
                        binding.layoutTier1.tvZeroQualityBuilding,
                        binding.layoutTier1.tvLowQualityBuilding,
                        binding.layoutTier1.tvHigherQualityBuilding,
                        binding.layoutTier1.tvExceedsQualityBuilding
                    )
                }
                R.id.tvHigherQualityBuilding -> {
                    qualityOfBuilding = 5
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvHigherQualityBuilding,
                        binding.layoutTier1.tvZeroQualityBuilding,
                        binding.layoutTier1.tvLowQualityBuilding,
                        binding.layoutTier1.tvMeetsQualityBuilding,
                        binding.layoutTier1.tvExceedsQualityBuilding
                    )
                }
                R.id.tvExceedsQualityBuilding -> {
                    qualityOfBuilding = 6
//                    countTier1AverageScore()
                    setAlpha(
                        binding.layoutTier1.tvExceedsQualityBuilding,
                        binding.layoutTier1.tvZeroQualityBuilding,
                        binding.layoutTier1.tvLowQualityBuilding,
                        binding.layoutTier1.tvMeetsQualityBuilding,
                        binding.layoutTier1.tvHigherQualityBuilding
                    )
                }

                R.id.btnTier1RatingSave -> {
                    var totalScore: Int
                    countTier1AverageScore()
                    if (location != 7 && streetAppeal != 7 && internalLayout != 7 && externalLayout != 7 && qualityOfBuilding != 7) {
                        totalScore =
                            location + streetAppeal + internalLayout + externalLayout + qualityOfBuilding
                        when (totalScore) {
                            in 0..5 -> {
                                averageScore = 0
                            }
                            in 6..12 -> {
                                averageScore = 1
                            }
                            in 13..16 -> {
                                averageScore = 3
                            }
                            in 17..25 -> {
                                averageScore = 5
                            }
                            in 26..30 -> {
                                averageScore = 6
                            }
                        }
                        if (isValidTier1()) {
                            myValuation =
                                Integer.parseInt(
                                    binding.layoutTier1.etMyValuation.text.toString()
                                        .replace(",", "")
                                )
                            marketValue =
                                Integer.parseInt(
                                    binding.layoutTier1.etMarketValue.text.toString()
                                        .replace(",", "")
                                )
                            Toast.makeText(this@BuyerPropertyDetailActivity,"Tier 1 Data Saved Successfully",Toast.LENGTH_SHORT).show()
                        } else {

                        }
                    } else {
                        setMessageComingFromServer(getString(R.string.val_ratings))
                    }
                }

                //Question 1
                R.id.tvQuestion1Yes -> {
                    que1 = 1
                    enableButton(binding.layoutQuestions.tvQuestion1Yes)
                    disableButton(binding.layoutQuestions.tvQuestion1No)
                }
                R.id.tvQuestion1No -> {
                    que1 = 0
                    enableButton(binding.layoutQuestions.tvQuestion1No)
                    disableButton(binding.layoutQuestions.tvQuestion1Yes)
                }

                //Question 2
                R.id.tvQuestion2Yes -> {
                    que2 = 1
                    enableButton(binding.layoutQuestions.tvQuestion2Yes)
                    disableButton(binding.layoutQuestions.tvQuestion2No)
                }
                R.id.tvQuestion2No -> {
                    que2 = 0
                    enableButton(binding.layoutQuestions.tvQuestion2No)
                    disableButton(binding.layoutQuestions.tvQuestion2Yes)
                }

                //Question 3
                R.id.tvQuestion3Yes -> {
                    que3 = 1
                    enableButton(binding.layoutQuestions.tvQuestion3Yes)
                    disableButton(binding.layoutQuestions.tvQuestion3No)
                    binding.layoutQuestions.etQue3Feedback.visibility =
                        View.VISIBLE    //Feedback field visible if press YES button
                }
                R.id.tvQuestion3No -> {
                    que3 = 0
                    enableButton(binding.layoutQuestions.tvQuestion3No)
                    disableButton(binding.layoutQuestions.tvQuestion3Yes)
                    binding.layoutQuestions.etQue3Feedback.visibility =
                        View.GONE       //Feedback field hide if press NO button
                }

                //Question 4
                R.id.tvQuestion4Email -> {
                    que4 = 0
                    if (!blEmail) enableButton(binding.layoutQuestions.tvQuestion4Email)
                    else disableButton(binding.layoutQuestions.tvQuestion4Email)

                    if (!que4List.contains("0"))
                        que4List.add("0")
                    else que4List.remove("0")

                    blEmail = !blEmail
//                    disableButton(binding.layoutTier1.tvQuestion4Phone)
//                    disableButton(binding.layoutTier1.tvQuestion4PropareMsg)
                }
                R.id.tvQuestion4Phone -> {
                    que4 = 1
                    if (!blPhone) enableButton(binding.layoutQuestions.tvQuestion4Phone)
                    else disableButton(binding.layoutQuestions.tvQuestion4Phone)

                    if (!que4List.contains("1"))
                        que4List.add("1")
                    else que4List.remove("1")

                    blPhone = !blPhone
//                    disableButton(binding.layoutTier1.tvQuestion4Email)
//                    disableButton(binding.layoutTier1.tvQuestion4PropareMsg)
                }
                R.id.tvQuestion4PropareMsg -> {
                    que4 = 2
                    if (!blMessenger) enableButton(binding.layoutQuestions.tvQuestion4PropareMsg)
                    else disableButton(binding.layoutQuestions.tvQuestion4PropareMsg)

                    if (!que4List.contains("2"))
                        que4List.add("2")
                    else que4List.remove("2")

                    blMessenger = !blMessenger
//                    disableButton(binding.layoutTier1.tvQuestion4Email)
//                    disableButton(binding.layoutTier1.tvQuestion4Phone)
                }

                //Tier 1 save & cancel
                R.id.btnSaveQue -> {
//                    if (isValidTier1()) {
                    que3Feed = binding.layoutQuestions.etQue3Feedback.text.toString()
                    myNotes = binding.layoutQuestions.etMyNotes.text.toString()
                    val listString: String = java.lang.String.join(",", que4List)

                    var ques4 = ""
//                        if (blEmail) ques4 = "0"
//                        if (blPhone && ques4 != "") ques4 += ",1" else ques4 = "1"
//                        if (blMessenger && ques4 != "") ques4 += ",2" else ques4 = "2"

                    if (que4List.isNotEmpty())
                        ques4 = listString

                    if (((que1 == 0 || que1 == 1) && (que2 == 0 || que2 == 1) && (que3 == 0 || que3 == 1) && ques4.isEmpty()) || que3 == 1 && que3Feed.isEmpty()) {
                        AlertDialogUtils.showSnakeBar(
                            resources.getString(R.string.msg_please_fill_all_the_details),
                            snackBar,
                            this
                        )
                    } else {
                        Toast.makeText(this@BuyerPropertyDetailActivity,"Data Saved Successfully",Toast.LENGTH_SHORT).show()
                    }
//                    }
                }

                //Tier 3 Create Template
                R.id.clCreateTemplate -> {
                    buyerPropertyDetailVM.mldTemplateType.value = 2
                    val intent = Intent(this, CreateTemplateActivity::class.java)
                    intent.putExtra(Keys.PROPERTY_ID, propertyId)
                    startActivity(intent)
                }
                R.id.tvPropareTemplate -> {
                    openSelectTemplateDialog()
                }
                R.id.btnSaveTier3 -> {
                    val setT3DataList = ArrayList<SetTier3DataRequest.Data>()
                    for (i in featuresList.indices) {
                        for (j in featuresList[i].categoryList!!.indices) {
//                            if (featuresList[i].categoryList!![j].like == 1 || featuresList[i].categoryList!![j].like == 2) {
                            val data = SetTier3DataRequest.Data(
                                featureId = featuresList[i].categoryList!![j].featureId,
                                like = featuresList[i].categoryList!![j].like
                            )
                            setT3DataList.add(data)
//                            }
                        }
                    }

                    if (setT3DataList.isNotEmpty()) {
                        Toast.makeText(this@BuyerPropertyDetailActivity,"Tier 3 Data Saved Successfully",Toast.LENGTH_SHORT).show()
                    } else {

                    }
                }
                else -> {}
            }
        }
    }

    private fun isValidTier1(): Boolean {
        val marketValue: String = binding.layoutTier1.etMarketValue.text.toString().trim()
        val myValuation: String = binding.layoutTier1.etMyValuation.text.toString().trim()

        if (TextUtils.isEmpty(marketValue)) {
            binding.layoutTier1.etMarketValue.requestFocus()
            KeyboardUtils.hideKeyboard(this, snackBar)
            AlertDialogUtils.showSnakeBar(
                resources.getString(R.string.msg_empty_market_value),
                snackBar,
                this
            )
            return false
        }

        if (TextUtils.isEmpty(myValuation)) {
            binding.layoutTier1.etMyValuation.requestFocus()
            KeyboardUtils.hideKeyboard(this, snackBar)
            AlertDialogUtils.showSnakeBar(
                resources.getString(R.string.msg_empty_my_valuation),
                snackBar,
                this
            )
            return false
        }
        return true
    }

    private fun setAlpha(
        tv1: RegularTextView,
        tv2: RegularTextView,
        tv3: RegularTextView,
        tv4: RegularTextView,
        tv5: RegularTextView
    ) {
        tv1.alpha = 1F
        tv2.alpha = 0.4F
        tv3.alpha = 0.4F
        tv4.alpha = 0.4F
        tv5.alpha = 0.4F
    }

    //Set the RecyclerView of Rating Score of Tier 2
    private fun setRecyclerViewTier2() {
        ratingScoreAdapter = RatingScoreAdapter(this, ratingScoreList, this)
        binding.layoutTier2.rvTier2.apply {
            this.adapter = ratingScoreAdapter
        }
    }

    //Set the List of Rating Score of Tier 2
    /*private fun setRatingScoreList() {
        ratingScoreList.clear()
        ratingScoreList.add(RatingScoreModel("Kitchen", 0, false))
        ratingScoreList.add(RatingScoreModel("Living Areas", 1, false))
        ratingScoreList.add(RatingScoreModel("Master Bedroom 1", 3, false))
        ratingScoreList.add(RatingScoreModel("Bedroom 2", 5, false))
        ratingScoreList.add(RatingScoreModel("Bedroom 3", 6, false))
        ratingScoreList.add(RatingScoreModel("Bedroom 4 + Others", 0, false))
        ratingScoreList.add(RatingScoreModel("Bathrooms", 1, false))
        ratingScoreList.add(RatingScoreModel("Media / Entertainment Room", 3, false))
        ratingScoreList.add(RatingScoreModel("Laundry", 5, false))
        ratingScoreList.add(RatingScoreModel("Study / Office", 6, false))
        ratingScoreList.add(RatingScoreModel("Storage", 0, false))
        ratingScoreList.add(RatingScoreModel("Parking / Garage / Carport", 1, false))
        ratingScoreList.add(RatingScoreModel("Patio / Deck", 3, false))
        ratingScoreList.add(RatingScoreModel("External Areas / Garden", 6, false))

         /*if(ratingScoreList.size==7){
            if(totalSum<=4){
                averageScore= ZERO_EXPECTATION
            }else if(totalSum>=5 && totalSum<=10){
                averageScore= LOWER_THAN_EXPECTATION
            }
        }*/

    }*/

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

    @SuppressLint("SetTextI18n")
    override fun updateProgressBar() {
        likeCount = 0
        disLikeCount = 0

        /*for (i in featuresList.indices) {
            for (j in 0 until featuresList[i].categoryList!!.size) {
                if (featuresList[i].categoryList!![j].visible == 0) {
                    featuresList[i].categoryList!!.remove(featuresList[i].categoryList!![j])
                }
            }
        }*/

        for (i in featuresList.indices) {
            for (j in featuresList[i].categoryList?.indices!!) {
                if (featuresList[i].categoryList!![j].visible != 0) {
                    if (featuresList[i].categoryList!![j].like == 1) {
                        likeCount++
                    } else if (featuresList[i].categoryList!![j].like == 2) {
                        disLikeCount++
                    }
                }
            }
        }

        val likeDislikeCount = likeCount + disLikeCount

        /*likepercentage = likeCount * 100 / totalFeatures
        disLikePercentage = disLikeCount * 100 / totalFeatures*/

        likepercentage = likeCount * 100 / 1.coerceAtLeast(likeDislikeCount)
        disLikePercentage = disLikeCount * 100 / 1.coerceAtLeast(likeDislikeCount)

        buyerPropertyDetailVM.likeCount.value = likeCount
        buyerPropertyDetailVM.likePercent.value = likepercentage
        buyerPropertyDetailVM.disLikeCount.value = disLikeCount
        buyerPropertyDetailVM.disLikePercent.value = disLikePercentage

        //Display progress
        binding.pbFeatures.progress = likepercentage

        //Display counts and percentages
        binding.tvLikeCounts.text = "$likeCount / $likepercentage%"
        binding.tvDisLikeCounts.text = "$disLikeCount / $disLikePercentage%"
    }

    // Open popup menu
    private fun openSelectTemplateDialog() {
        val inflater =
            applicationContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogBinding: DialogPropareTemplateBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_propare_template, null, false)

        val popupWindow = PopupWindow(
            dialogBinding.root,
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        if (defaultTemplate == 1) {
            dialogBinding.tvPropareTemplate.setTextColor(getColor(R.color.color_green))
            dialogBinding.tvCustomTemplate.setTextColor(getColor(R.color.black))
            dialogBinding.rbPropareTemplate.isChecked = true
            dialogBinding.rbCustomTemplate.isChecked = false
            buyerPropertyDetailVM.mldTemplateType.value = 1
        } else if (defaultTemplate == 2) {
            buyerPropertyDetailVM.mldTemplateType.value = 2
            dialogBinding.tvPropareTemplate.setTextColor(getColor(R.color.black))
            dialogBinding.tvCustomTemplate.setTextColor(getColor(R.color.color_green))
            dialogBinding.rbCustomTemplate.isChecked = true
            dialogBinding.rbPropareTemplate.isChecked = false
        }

        dialogBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (dialogBinding.rbPropareTemplate.isChecked) {
                dialogBinding.tvPropareTemplate.setTextColor(getColor(R.color.color_green))
                dialogBinding.tvCustomTemplate.setTextColor(getColor(R.color.black))
                buyerPropertyDetailVM.mldTemplateType.value = 1
            } else {
                buyerPropertyDetailVM.mldTemplateType.value = 2
                dialogBinding.tvPropareTemplate.setTextColor(getColor(R.color.black))
                dialogBinding.tvCustomTemplate.setTextColor(getColor(R.color.color_green))
            }
        }

        dialogBinding.rbPropareTemplate.setOnClickListener {
            buyerPropertyDetailVM.mldTemplateType.value = 1
            dialogBinding.rbPropareTemplate.isChecked = true
            dialogBinding.rbCustomTemplate.isChecked = false
            popupWindow.dismiss()
            callTier3Data()

        }

        dialogBinding.rbCustomTemplate.setOnClickListener {
            buyerPropertyDetailVM.mldTemplateType.value = 2
            dialogBinding.rbCustomTemplate.isChecked = true
            dialogBinding.rbPropareTemplate.isChecked = false
            popupWindow.dismiss()
            callTier3Data()
        }

        dialogBinding.tvCustomTemplate.setOnClickListener {
            popupWindow.dismiss()
//            buyerPropertyDetailVM.mldTemplateType.value = 2
            val intent = Intent(this, CreateTemplateActivity::class.java)
            intent.putExtra(Keys.PROPERTY_ID, propertyId)
            startActivity(intent)
        }

        popupWindow.showAsDropDown(binding.tvPropareTemplate)
    }
    // Open popup menu End

    override fun onCompareClick() {

    }

    override fun showProgress() {
        showProgressDialog()
    }

    override fun hideProgress() {
        hideProgressDialog()
    }

    override fun setMessageComingFromServer(message: String?) {
//        AlertDialogUtils.showAlert(this, message)
        AlertDialogUtils.showSnakeBar(message, snackBar, this)
    }

    override fun onQRClick() {
        if (propertyList.propertyQRImage != null)
            AlertDialogUtils.showQRImagePopup(this, propertyList.propertyQRImage)
    }

    //    Get Tier 1 API Call
    private fun getTier1Details(propertyId: Int) {
//        buyerPropertyDetailVM.getTier1Details(propertyId)
        buyerPropertyDetailVM.tier1Details.observe(this) { tier1Data ->
            tier1Data.let {
                binding.layoutTier1.tvTier2Title.requestFocus()
//                binding.layoutQuestions.tvQuestionsTitle.requestFocus()
                binding.nsv.scrollTo(0, 0)
                if (it.details != null) {

                    if (it.details.question1 == 1) {
                        que1 = 1
                        enableButton(binding.layoutQuestions.tvQuestion1Yes)
                        disableButton(binding.layoutQuestions.tvQuestion1No)
                    } else if (it.details.question1 == 0) {
                        que1 = 0
                        enableButton(binding.layoutQuestions.tvQuestion1No)
                        disableButton(binding.layoutQuestions.tvQuestion1Yes)
                    }
                    if (it.details.question2 == 1) {
                        que2 = 1
                        enableButton(binding.layoutQuestions.tvQuestion2Yes)
                        disableButton(binding.layoutQuestions.tvQuestion2No)
                    } else if (it.details.question2 == 0) {
                        que2 = 0
                        enableButton(binding.layoutQuestions.tvQuestion2No)
                        disableButton(binding.layoutQuestions.tvQuestion2Yes)
                    }
                    if (it.details.question3 == 1) {
                        que3 = 1
                        enableButton(binding.layoutQuestions.tvQuestion3Yes)
                        disableButton(binding.layoutQuestions.tvQuestion3No)
                        binding.layoutQuestions.etQue3Feedback.setText(it.details.question3Feedback)
                        binding.layoutQuestions.etQue3Feedback.visibility =
                            View.VISIBLE
                    } else if (it.details.question3 == 0) {
                        que3 = 0
                        enableButton(binding.layoutQuestions.tvQuestion3No)
                        disableButton(binding.layoutQuestions.tvQuestion3Yes)
                        binding.layoutQuestions.etQue3Feedback.visibility =
                            View.GONE
                    }

                    if (!TextUtils.isEmpty(it.details.question4)) {
                        if (!it.details.question4.toString().contains(",")) {
                            que4List.add(it.details.question4.toString())
                        } else {
                            if (it.details.question4!!.length == 2 &&
                                it.details.question4!!.startsWith(",") ||
                                it.details.question4!!.endsWith(",")
                            ) {
                                que4List.add(it.details.question4!!.replace(",", ""))
                            } else {
                                que4List =
                                    GlobalMethods.stringToWords(it.details.question4.toString())
                            }
                        }

                        if (que4List.isNotEmpty()) {
                            for (i in que4List.indices) {
                                when (que4List[i]) {
                                    "0" -> {
                                        que4 = 0
//                                        if (!blEmail)
                                        enableButton(binding.layoutQuestions.tvQuestion4Email)
//                                        else disableButton(binding.layoutTier1.tvQuestion4Email)

                                        blEmail = true
                                        //                    disableButton(binding.layoutTier1.tvQuestion4Phone)
                                        //                    disableButton(binding.layoutTier1.tvQuestion4PropareMsg)
                                    }
                                    "1" -> {
                                        que4 = 1
//                                        if (!blPhone)
                                        enableButton(binding.layoutQuestions.tvQuestion4Phone)
//                                        else disableButton(binding.layoutTier1.tvQuestion4Phone)

                                        blPhone = true
                                        //                    disableButton(binding.layoutTier1.tvQuestion4Email)
                                        //                    disableButton(binding.layoutTier1.tvQuestion4PropareMsg)
                                    }
                                    "2" -> {
                                        que4 = 2
//                                        if (!blMessenger)
                                        enableButton(binding.layoutQuestions.tvQuestion4PropareMsg)
//                                        else disableButton(binding.layoutTier1.tvQuestion4PropareMsg)
                                        blMessenger = true

                                        //                    disableButton(binding.layoutTier1.tvQuestion4Email)
                                        //                    disableButton(binding.layoutTier1.tvQuestion4Phone)
                                    }
                                }
                            }
                        }
                    }

                    binding.layoutQuestions.etMyNotes.setText(it.details.myNotes)
                }

                if (it.scoring != null) {
//                    if (!TextUtils.isEmpty(it.scoring.location.toString())) {
                    binding.layoutTier1.etMarketValue.setText(it.scoring.marketValue)
                    binding.layoutTier1.etMyValuation.setText(it.scoring.myValuation)

                    when (it.scoring.location) {
                        0 -> {
                            location = 0
                            setAlpha(
                                binding.layoutTier1.tvZeroLocation,
                                binding.layoutTier1.tvLowLocation,
                                binding.layoutTier1.tvMeetsLocation,
                                binding.layoutTier1.tvHigherLocation,
                                binding.layoutTier1.tvExceedsLocation
                            )
                        }
                        1 -> {
                            location = 1
                            setAlpha(
                                binding.layoutTier1.tvLowLocation,
                                binding.layoutTier1.tvZeroLocation,
                                binding.layoutTier1.tvMeetsLocation,
                                binding.layoutTier1.tvHigherLocation,
                                binding.layoutTier1.tvExceedsLocation
                            )
                        }
                        3 -> {
                            location = 3
                            setAlpha(
                                binding.layoutTier1.tvMeetsLocation,
                                binding.layoutTier1.tvZeroLocation,
                                binding.layoutTier1.tvLowLocation,
                                binding.layoutTier1.tvHigherLocation,
                                binding.layoutTier1.tvExceedsLocation
                            )
                        }
                        5 -> {
                            location = 5
                            setAlpha(
                                binding.layoutTier1.tvHigherLocation,
                                binding.layoutTier1.tvZeroLocation,
                                binding.layoutTier1.tvLowLocation,
                                binding.layoutTier1.tvMeetsLocation,
                                binding.layoutTier1.tvExceedsLocation
                            )
                        }
                        6 -> {
                            location = 6
                            setAlpha(
                                binding.layoutTier1.tvExceedsLocation,
                                binding.layoutTier1.tvZeroLocation,
                                binding.layoutTier1.tvLowLocation,
                                binding.layoutTier1.tvMeetsLocation,
                                binding.layoutTier1.tvHigherLocation
                            )
                        }
                    }
//                    }

//                    if (!TextUtils.isEmpty(it.scoring.streetAppeal.toString())) {
                    when (it.scoring.streetAppeal) {
                        0 -> {
                            streetAppeal = 0
                            setAlpha(
                                binding.layoutTier1.tvZeroStreetAppeal,
                                binding.layoutTier1.tvLowStreetAppeal,
                                binding.layoutTier1.tvMeetsStreetAppeal,
                                binding.layoutTier1.tvHigherStreetAppeal,
                                binding.layoutTier1.tvExceedsStreetAppeal
                            )
                        }
                        1 -> {
                            streetAppeal = 1
                            setAlpha(
                                binding.layoutTier1.tvLowStreetAppeal,
                                binding.layoutTier1.tvZeroStreetAppeal,
                                binding.layoutTier1.tvMeetsStreetAppeal,
                                binding.layoutTier1.tvHigherStreetAppeal,
                                binding.layoutTier1.tvExceedsStreetAppeal
                            )
                        }
                        3 -> {
                            streetAppeal = 3
                            setAlpha(
                                binding.layoutTier1.tvMeetsStreetAppeal,
                                binding.layoutTier1.tvZeroStreetAppeal,
                                binding.layoutTier1.tvLowStreetAppeal,
                                binding.layoutTier1.tvHigherStreetAppeal,
                                binding.layoutTier1.tvExceedsStreetAppeal
                            )
                        }
                        5 -> {
                            streetAppeal = 5
                            setAlpha(
                                binding.layoutTier1.tvHigherStreetAppeal,
                                binding.layoutTier1.tvZeroStreetAppeal,
                                binding.layoutTier1.tvLowStreetAppeal,
                                binding.layoutTier1.tvMeetsStreetAppeal,
                                binding.layoutTier1.tvExceedsStreetAppeal
                            )
                        }
                        6 -> {
                            streetAppeal = 6
                            setAlpha(
                                binding.layoutTier1.tvExceedsStreetAppeal,
                                binding.layoutTier1.tvZeroStreetAppeal,
                                binding.layoutTier1.tvLowStreetAppeal,
                                binding.layoutTier1.tvMeetsStreetAppeal,
                                binding.layoutTier1.tvHigherStreetAppeal
                            )
                        }
                    }
//                    }

//                    if (!TextUtils.isEmpty(it.scoring.internalLayout.toString())) {
                    when (it.scoring.internalLayout) {
                        0 -> {
                            internalLayout = 0
                            setAlpha(
                                binding.layoutTier1.tvZeroInternalLayout,
                                binding.layoutTier1.tvLowInternalLayout,
                                binding.layoutTier1.tvMeetsInternalLayout,
                                binding.layoutTier1.tvHigherInternalLayout,
                                binding.layoutTier1.tvExceedsInternalLayout
                            )
                        }
                        1 -> {
                            internalLayout = 1
                            setAlpha(
                                binding.layoutTier1.tvLowInternalLayout,
                                binding.layoutTier1.tvZeroInternalLayout,
                                binding.layoutTier1.tvMeetsInternalLayout,
                                binding.layoutTier1.tvHigherInternalLayout,
                                binding.layoutTier1.tvExceedsInternalLayout
                            )
                        }
                        3 -> {
                            internalLayout = 3
                            setAlpha(
                                binding.layoutTier1.tvMeetsInternalLayout,
                                binding.layoutTier1.tvZeroInternalLayout,
                                binding.layoutTier1.tvLowInternalLayout,
                                binding.layoutTier1.tvHigherInternalLayout,
                                binding.layoutTier1.tvExceedsInternalLayout
                            )
                        }
                        5 -> {
                            internalLayout = 5
                            setAlpha(
                                binding.layoutTier1.tvHigherInternalLayout,
                                binding.layoutTier1.tvZeroInternalLayout,
                                binding.layoutTier1.tvLowInternalLayout,
                                binding.layoutTier1.tvMeetsInternalLayout,
                                binding.layoutTier1.tvExceedsInternalLayout
                            )
                        }
                        6 -> {
                            internalLayout = 6
                            setAlpha(
                                binding.layoutTier1.tvExceedsInternalLayout,
                                binding.layoutTier1.tvZeroInternalLayout,
                                binding.layoutTier1.tvLowInternalLayout,
                                binding.layoutTier1.tvMeetsInternalLayout,
                                binding.layoutTier1.tvHigherInternalLayout
                            )
                        }
                    }
//                    }

//                    if (!TextUtils.isEmpty(it.scoring.externalLayout.toString())) {
                    when (it.scoring.externalLayout) {
                        0 -> {
                            externalLayout = 0
                            setAlpha(
                                binding.layoutTier1.tvZeroExternalLayout,
                                binding.layoutTier1.tvLowExternalLayout,
                                binding.layoutTier1.tvMeetsExternalLayout,
                                binding.layoutTier1.tvHigherExternalLayout,
                                binding.layoutTier1.tvExceedsExternalLayout
                            )
                        }
                        1 -> {
                            externalLayout = 1
                            setAlpha(
                                binding.layoutTier1.tvLowExternalLayout,
                                binding.layoutTier1.tvZeroExternalLayout,
                                binding.layoutTier1.tvMeetsExternalLayout,
                                binding.layoutTier1.tvHigherExternalLayout,
                                binding.layoutTier1.tvExceedsExternalLayout
                            )
                        }
                        3 -> {
                            externalLayout = 3
                            setAlpha(
                                binding.layoutTier1.tvMeetsExternalLayout,
                                binding.layoutTier1.tvZeroExternalLayout,
                                binding.layoutTier1.tvLowExternalLayout,
                                binding.layoutTier1.tvHigherExternalLayout,
                                binding.layoutTier1.tvExceedsExternalLayout
                            )
                        }
                        5 -> {
                            externalLayout = 5
                            setAlpha(
                                binding.layoutTier1.tvHigherExternalLayout,
                                binding.layoutTier1.tvZeroExternalLayout,
                                binding.layoutTier1.tvLowExternalLayout,
                                binding.layoutTier1.tvMeetsExternalLayout,
                                binding.layoutTier1.tvExceedsExternalLayout
                            )
                        }
                        6 -> {
                            externalLayout = 6
                            setAlpha(
                                binding.layoutTier1.tvExceedsExternalLayout,
                                binding.layoutTier1.tvZeroExternalLayout,
                                binding.layoutTier1.tvLowExternalLayout,
                                binding.layoutTier1.tvMeetsExternalLayout,
                                binding.layoutTier1.tvHigherExternalLayout
                            )
                        }
                    }
//                    }

//                    if (!TextUtils.isEmpty(it.scoring.qualityOfBuilding.toString())) {
                    when (it.scoring.qualityOfBuilding) {
                        0 -> {
                            qualityOfBuilding = 0
                            setAlpha(
                                binding.layoutTier1.tvZeroQualityBuilding,
                                binding.layoutTier1.tvLowQualityBuilding,
                                binding.layoutTier1.tvMeetsQualityBuilding,
                                binding.layoutTier1.tvHigherQualityBuilding,
                                binding.layoutTier1.tvExceedsQualityBuilding
                            )
                        }
                        1 -> {
                            qualityOfBuilding = 1
                            setAlpha(
                                binding.layoutTier1.tvLowQualityBuilding,
                                binding.layoutTier1.tvZeroQualityBuilding,
                                binding.layoutTier1.tvMeetsQualityBuilding,
                                binding.layoutTier1.tvHigherQualityBuilding,
                                binding.layoutTier1.tvExceedsQualityBuilding
                            )
                        }
                        3 -> {
                            qualityOfBuilding = 3
                            setAlpha(
                                binding.layoutTier1.tvMeetsQualityBuilding,
                                binding.layoutTier1.tvZeroQualityBuilding,
                                binding.layoutTier1.tvLowQualityBuilding,
                                binding.layoutTier1.tvHigherQualityBuilding,
                                binding.layoutTier1.tvExceedsQualityBuilding
                            )
                        }
                        5 -> {
                            qualityOfBuilding = 5
                            setAlpha(
                                binding.layoutTier1.tvHigherQualityBuilding,
                                binding.layoutTier1.tvZeroQualityBuilding,
                                binding.layoutTier1.tvLowQualityBuilding,
                                binding.layoutTier1.tvMeetsQualityBuilding,
                                binding.layoutTier1.tvExceedsQualityBuilding
                            )
                        }
                        6 -> {
                            qualityOfBuilding = 6
                            setAlpha(
                                binding.layoutTier1.tvExceedsQualityBuilding,
                                binding.layoutTier1.tvZeroQualityBuilding,
                                binding.layoutTier1.tvLowQualityBuilding,
                                binding.layoutTier1.tvMeetsQualityBuilding,
                                binding.layoutTier1.tvHigherQualityBuilding
                            )
                        }
                    }
//                    }

//                    if (!TextUtils.isEmpty(it.scoring.averageScore.toString())) {
                    when (it.scoring.averageScore) {
                        0 -> {
                            setZeroScoreT1()
                        }
                        1 -> {
                            setLowScoreT1()
                        }
                        3 -> {
                            setMeetScoreT1()
                        }
                        5 -> {
                            setHigherScoreT1()
                        }
                        6 -> {
                            setExceedScoreT1()
                        }
                    }
//                    }
                }
            }
        }
    }

    //    Get Tier 2 API Call
    private fun getTier2Details(propertyId: Int) {
//        buyerPropertyDetailVM.getTier2Details(propertyId)

        ratingScoreList.clear()
        ratingScoreList.add(RatingScoreModel(0, "Kitchen", 0, false, true))
        ratingScoreList.add(
            RatingScoreModel(
                1, "Living Areas", 0, false, true
            )
        )
        ratingScoreList.add(
            RatingScoreModel(
                2, "Master Bedroom 1", 0, false, true
            )
        )
        ratingScoreList.add(
            RatingScoreModel(
                3,
                "Bedroom 2",
                0,
                false,
                false
            )
        )
        ratingScoreList.add(
            RatingScoreModel(
                4,
                "Bedroom 3",
                0,
                false,
                false
            )
        )
        ratingScoreList.add(
            RatingScoreModel(
                5, "Bedroom 4 + Others", 0, false, false
            )
        )
        ratingScoreList.add(
            RatingScoreModel(
                6,
                "Bathrooms",
                0,
                false,
                true
            )
        )
        ratingScoreList.add(
            RatingScoreModel(
                7,
                "Media / Entertainment Room",
                0,
                false,
                false
            )
        )
        ratingScoreList.add(RatingScoreModel(8, "Laundry", 0, false, true))
        ratingScoreList.add(
            RatingScoreModel(
                9,
                "Study / Office",
                0,
                false,
                false
            )
        )
        ratingScoreList.add(RatingScoreModel(10, "Storage", 0, false, true))
        ratingScoreList.add(
            RatingScoreModel(
                11,
                "Parking / Garage / Carport",
                0,
                false,
                true
            )
        )
        ratingScoreList.add(
            RatingScoreModel(
                12,
                "Patio / Deck",
                0,
                false,
                false
            )
        )
        ratingScoreList.add(
            RatingScoreModel(
                13,
                "External Areas / Garden",
                0,
                false,
                false
            )
        )
        /*setRatingScoreList()*/
        setRecyclerViewTier2()
        binding.layoutTier2.btnSaveTier2.setOnClickListener {
            Toast.makeText(this,"Tier 2 Data save successfully",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setExceedScoreT2() {
        binding.layoutTier2.ivRatingScoreAverage.setImageResource(R.drawable.ic_exceeds)
        binding.layoutTier2.tvRatingText.text =
            getString(R.string.exceeds_the_expectations)
    }

    private fun setHigherScoreT2() {
        binding.layoutTier2.ivRatingScoreAverage.setImageResource(R.drawable.ic_higher)
        binding.layoutTier2.tvRatingText.text =
            getString(R.string.higher_than_expectations)
    }

    private fun setMeetScoreT2() {
        binding.layoutTier2.ivRatingScoreAverage.setImageResource(R.drawable.ic_meets)
        binding.layoutTier2.tvRatingText.text =
            getString(R.string.meet_the_expectations)
    }

    private fun setLowerScoreT2() {
        binding.layoutTier2.ivRatingScoreAverage.setImageResource(R.drawable.ic_low)
        binding.layoutTier2.tvRatingText.text =
            getString(R.string.lower_than_expectations)
    }

    private fun setZeroScoreT2() {
        binding.layoutTier2.ivRatingScoreAverage.setImageResource(R.drawable.ic_zero)
        binding.layoutTier2.tvRatingText.text =
            getString(R.string.zero_expectations)
    }

    private fun setExceedScoreT1() {
        averageScore = 6
        binding.layoutTier1.ivRatingScoreAverage.setImageResource(R.drawable.ic_exceeds)
        binding.layoutTier1.tvRatingText.text =
            getString(R.string.exceeds_the_expectations)
    }

    private fun setHigherScoreT1() {
        averageScore = 5
        binding.layoutTier1.ivRatingScoreAverage.setImageResource(R.drawable.ic_higher)
        binding.layoutTier1.tvRatingText.text =
            getString(R.string.higher_than_expectations)
    }

    private fun setMeetScoreT1() {
        averageScore = 3
        binding.layoutTier1.ivRatingScoreAverage.setImageResource(R.drawable.ic_meets)
        binding.layoutTier1.tvRatingText.text =
            getString(R.string.meet_the_expectations)
    }

    private fun setLowScoreT1() {
        averageScore = 1
        binding.layoutTier1.ivRatingScoreAverage.setImageResource(R.drawable.ic_low)
        binding.layoutTier1.tvRatingText.text =
            getString(R.string.lower_than_expectations)
    }

    private fun setZeroScoreT1() {
        averageScore = 0
        binding.layoutTier1.ivRatingScoreAverage.setImageResource(R.drawable.ic_zero)
        binding.layoutTier1.tvRatingText.text =
            getString(R.string.zero_expectations)
    }

    private fun countTier1AverageScore(): Int {
        val loc = if (location == 0) 0
        else location

        val street = if (streetAppeal == 0) 0
        else streetAppeal

        val internal = if (internalLayout == 0) 0
        else internalLayout

        val external = if (externalLayout == 0) 0
        else externalLayout

        val quality = if (qualityOfBuilding == 0) 0
        else qualityOfBuilding

//        if (loc != 0 && street != 0 && internal != 0 && external != 0 && quality != 0) {
        val average = loc + street + internal + external + quality
        when (average) {
            in 0..5 -> {
                setZeroScoreT1()
            }
            in 6..12 -> {
                setLowScoreT1()
            }
            in 13..16 -> {
                setMeetScoreT1()
            }
            in 17..25 -> {
                setHigherScoreT1()
            }
            in 26..30 -> {
                setExceedScoreT1()
            }
//            }
        }

        Log.e("AVERAGE_SCORE", average.toString())

        return average
    }


}