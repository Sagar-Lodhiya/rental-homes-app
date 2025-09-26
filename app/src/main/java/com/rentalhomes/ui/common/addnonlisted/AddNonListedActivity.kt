package com.rentalhomes.ui.common.addnonlisted

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.model.responseModel.GetPropertyListBuyerResponse
import com.rentalhomes.databinding.ActivityAddNonListedBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods
import com.rentalhomes.utils.ImagePickerActivity
import com.rentalhomes.utils.KeyboardUtils
import java.io.File

/**
 * Created by Malhar
 * */
class AddNonListedActivity : BaseActivity<ActivityAddNonListedBinding, AddNonListedVM>(),
    AddNonListedNavigator,
    View.OnClickListener {
    private lateinit var binding: ActivityAddNonListedBinding
    private lateinit var addNonListedVM: AddNonListedVM
    private lateinit var snackBar: View

    private var propertyPhoto: File? = null

    private var counterBed: Int = 0
    private var counterBath: Int = 0
    private var counterCar: Int = 0
    private lateinit var type: String
//    lateinit var nonListedList: GetNonListedResponse.Data
    lateinit var nonListedList: GetPropertyListBuyerResponse.Data
    private var profilePic: File? = null

    private var REQUEST_CODE_AUTOCOMPLETE: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setOnListener()
        setHeader()

    }

    private fun init() {
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)
        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this
            it.addNonListedVM = addNonListedVM
        }
        addNonListedVM.attachContext(this)
        addNonListedVM.setNavigator(this)
        snackBar = findViewById(android.R.id.content)
        type = intent.getStringExtra(Keys.TYPE).toString()
    }

    override val bindingVariable: Int
        get() = BR.addNonListedVM
    override val layoutId: Int
        get() = R.layout.activity_add_non_listed
    override val viewModel: AddNonListedVM
        get() {
            addNonListedVM = ViewModelProvider(this).get(AddNonListedVM::class.java)
            return addNonListedVM
        }

    override fun setHeader() {
        if (type == "1") {
            binding.headerBar.tvTitle.text = getString(R.string.non_listed_properties1)
        } else if (type == "2") {
            nonListedList = intent.getParcelableExtra(Keys.NON_LISTED)!!
            binding.headerBar.tvTitle.text = getString(R.string.edit_non_listed_property)
            addNonListedVM.mldPropertyId.value = nonListedList.propertyId
            //Hide the views of camera & gallery chooser
            binding.clGalleryCamera.visibility = View.GONE
            //Shows the imageView and textView of tap to change photo
            binding.cvPropertyPhoto.visibility = View.VISIBLE
            binding.tvTapToChangePhoto.visibility = View.VISIBLE
            Glide.with(this).load(nonListedList.propertyImage).into(binding.ivPropertyImage)
            addNonListedVM.mldPropertyImage.value = File(nonListedList.propertyImage.toString())
            propertyPhoto = File(nonListedList.propertyImage.toString())
            binding.etAddress.setText(nonListedList.propertyAddress.toString())
            binding.etSuburb.setText(nonListedList.propertyCity.toString())
            binding.etLandSize.setText(nonListedList.landSize.toString())
            binding.tvCountBed.setText(nonListedList.bed.toString())
            counterBed = nonListedList.bed.toString().toInt()
            binding.tvCountBath.setText(nonListedList.bath.toString())
            counterBath = nonListedList.bath.toString().toInt()
            binding.tvCountCar.setText(nonListedList.car.toString())
            counterCar = nonListedList.car.toString().toInt()
            binding.etDescription.setText(nonListedList.description.toString())
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
        onBackPressed()
    }

    private fun openPlacePicker() {
        // Initialize Places.
        Places.initialize(
            this, getString(R.string.google_maps_key)
        )
        // Create a new Places client instance.
        Places.createClient(this)
        // Set the fields to specify which types of place data to return.
        val fields: List<Place.Field> = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS,
            Place.Field.ID,
            Place.Field.PHONE_NUMBER,
            Place.Field.RATING,
            Place.Field.WEBSITE_URI
        )

        // Start the autocomplete intent.
        val intent: Intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN,
            fields
        ).setTypeFilter(TypeFilter.CITIES).setCountry("AU").build(this)

        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) {
            return
        }
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            when (resultCode) {
                RESULT_OK -> {
                    // Get Place data from intent
                    val place = Autocomplete.getPlaceFromIntent(data)
                    binding.etSuburb.setText(place.name)
                    KeyboardUtils.hideKeyboard(this, snackBar)
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    val status = Autocomplete.getStatusFromIntent(data)
                    //Log.i("place_address_status", status.getStatusMessage());
                }
                RESULT_CANCELED -> {
                    // The user canceled the operation.
                    Toast.makeText(this, "Result got cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //Suburb validation
    fun isValidSuburb(): Boolean {
        val suburb: String = binding.etSuburb.text.toString().trim()

        if (TextUtils.isEmpty(suburb)) {
            binding.tvValSuburb.text = resources.getString(R.string.msg_empty_suburb)
            return false
        }

        if (suburb.length < 2) {
            binding.tvValSuburb.text = resources.getString(R.string.msg_suburb_length)
            return false
        }
        return true
    }

    private fun setOnListener() {
        binding.headerBar.ivBack.setOnClickListener(this)

        //Suburb click
        binding.etSuburb.setOnClickListener {
            openPlacePicker()
        }


        binding.etSuburb.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Check and show validation of First Name
                if (!isValidSuburb() && p0.toString().isNotEmpty()) {
                    binding.tvValSuburb.visibility = View.VISIBLE
                } else {
                    binding.tvValSuburb.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (isValidSuburb() && isValidAddress() && isValidLandSize() && isValidDescription()) {
                    GlobalMethods.enableButton(this@AddNonListedActivity, binding.btnSave)
                } else {
                    GlobalMethods.disableButton(this@AddNonListedActivity, binding.btnSave)
                }
            }

        })

        //Address TextWatcher
        binding.etAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Check and show validation of First Name
                if (!isValidAddress() && p0.toString().isNotEmpty()) {
                    binding.tvValAddress.visibility = View.VISIBLE
                } else {
                    binding.tvValAddress.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (isValidAddress() && isValidLandSize() && isValidDescription() && isValidSuburb()) {
                    GlobalMethods.enableButton(this@AddNonListedActivity, binding.btnSave)
                } else {
                    GlobalMethods.disableButton(this@AddNonListedActivity, binding.btnSave)
                }
            }

        })

        //Land Size TextWatcher
        binding.etLandSize.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Check and show validation of First Name
                if (!isValidLandSize() && p0.toString().isNotEmpty()) {
                    binding.tvValLandSize.visibility = View.VISIBLE
                } else {
                    binding.tvValLandSize.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (isValidAddress() && isValidLandSize() && isValidDescription()) {
                    GlobalMethods.enableButton(this@AddNonListedActivity, binding.btnSave)
                } else {
                    GlobalMethods.disableButton(this@AddNonListedActivity, binding.btnSave)
                }
            }

        })

        //Description TextWatcher
        binding.etDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Check and show validation of First Name
                if (!isValidDescription() && p0.toString().isNotEmpty()) {
                    binding.tvValDescription.visibility = View.VISIBLE
                } else {
                    binding.tvValDescription.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (isValidAddress() && isValidLandSize() && isValidDescription()) {
                    GlobalMethods.enableButton(this@AddNonListedActivity, binding.btnSave)
                } else {
                    GlobalMethods.disableButton(this@AddNonListedActivity, binding.btnSave)
                }
            }

        })
        /*if (RentalHomesApp.appSessionManager.getInt(
                this,
                AppSessionManager.PREF_NAME,
                AppSessionManager.PREF_KEY_USER_TYPE
            ) == 0
        )*/if (type == "0") {
            binding.clAgentInformation.visibility = View.VISIBLE
            //First Name TextWatcher
            binding.etFirstName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //Check and show validation of First Name
                    if (!isValidFirstName() && p0.toString().isNotEmpty()) {
                        binding.tvValFirstName.visibility = View.VISIBLE
                    } else {
                        binding.tvValFirstName.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (isValidAddress() && isValidLandSize() && isValidDescription() && isValidFirstName() && isValidLastName() && isValidEmail() && isValidPhoneLength()) {
                        GlobalMethods.enableButton(this@AddNonListedActivity, binding.btnSave)
                    } else {
                        GlobalMethods.disableButton(this@AddNonListedActivity, binding.btnSave)
                    }
                }

            })

            //Last Name TextWatcher
            binding.etLastName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //Check and show validation of First Name
                    if (!isValidLastName() && p0.toString().isNotEmpty()) {
                        binding.tvValLastName.visibility = View.VISIBLE
                    } else {
                        binding.tvValLastName.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (isValidAddress() && isValidLandSize() && isValidDescription() && isValidFirstName() && isValidLastName() && isValidEmail() && isValidPhoneLength()) {
                        GlobalMethods.enableButton(this@AddNonListedActivity, binding.btnSave)
                    } else {
                        GlobalMethods.disableButton(this@AddNonListedActivity, binding.btnSave)
                    }
                }

            })

            //Email TextWatcher
            binding.etEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //Check and show validation of First Name
                    if (!isValidEmail() && p0.toString().isNotEmpty()) {
                        binding.tvValEmail.visibility = View.VISIBLE
                    } else {
                        binding.tvValEmail.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (isValidAddress() && isValidLandSize() && isValidDescription() && isValidFirstName() && isValidLastName() && isValidEmail() && isValidPhoneLength()) {
                        GlobalMethods.enableButton(this@AddNonListedActivity, binding.btnSave)
                    } else {
                        GlobalMethods.disableButton(this@AddNonListedActivity, binding.btnSave)
                    }
                }

            })

            //Mobile TextWatcher
            binding.etPhone.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //Check and show validation of First Name
                    if (!isValidPhone() && p0.toString().isNotEmpty()) {
                        binding.tvValPhone.visibility = View.VISIBLE
                    } else {
                        binding.tvValPhone.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (isValidAddress() && isValidLandSize() && isValidDescription() && isValidFirstName() && isValidLastName() && isValidEmail() && isValidPhone()) {
                        GlobalMethods.enableButton(this@AddNonListedActivity, binding.btnSave)
                    } else {
                        GlobalMethods.disableButton(this@AddNonListedActivity, binding.btnSave)
                    }
                }

            })
        } else {
            binding.clAgentInformation.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        v?.let { view ->
            when (view.id) {
                R.id.ivBack -> {
                    KeyboardUtils.hideKeyboard(this, snackBar)
                    onBackPressed()
                }
            }
        }
    }

    override fun onSaveClicked() {
        if (type == "1") {
            if (isValidPropertyPhoto() && isValidAddress() && isValidDescription() && isValidLandSize() && isValidSuburb()) {
                addNonListedVM.mldSuburb.value = binding.etSuburb.text.toString()
                addNonListedVM.mldBathCount.value = binding.tvCountBath.text.toString().toInt()
                addNonListedVM.mldBedCount.value = binding.tvCountBed.text.toString().toInt()
                addNonListedVM.mldCarCount.value = binding.tvCountCar.text.toString().toInt()
                addNonListedVM.mldLandSize.value = binding.etLandSize.text.toString().toInt()
                addNonListedVM.addNonListedCall()
            }
        } else if (type == "2") {
            if (isValidPropertyPhoto() && isValidAddress() && isValidDescription() && isValidLandSize() && isValidSuburb()) {
//                addNonListedVM.mldAddress.value = binding.etAddress.text.toString()
//                addNonListedVM.mldDescription.value = binding.etDescription.text.toString()
                addNonListedVM.mldSuburb.value = binding.etSuburb.text.toString()
                addNonListedVM.mldBathCount.value = binding.tvCountBath.text.toString().toInt()
                addNonListedVM.mldBedCount.value = binding.tvCountBed.text.toString().toInt()
                addNonListedVM.mldCarCount.value = binding.tvCountCar.text.toString().toInt()
                addNonListedVM.mldLandSize.value = binding.etLandSize.text.toString().toInt()
                addNonListedVM.editNonListedCall()
            }
        }
    }

    override fun onGalleryClicked() {
        openGallery()
        Log.e(
            "HW",
            "Ratio: " + binding.clChoosePhoto.measuredWidth.toInt() + "Height  " + binding.clChoosePhoto.measuredHeight.toInt()
        )
    }

    override fun onCameraClicked() {
        Log.e(
            "HW",
            "Ratio: " + binding.clChoosePhoto.measuredWidth.toInt() + "Height  " + binding.clChoosePhoto.measuredHeight.toInt()
        )
        openCamera()
    }

    override fun onPropertyPhotoClicked() {
        showImageChooserDialog()
    }

    /*Counter clicks start*/
    override fun onMinusBedClicked() {
        if (counterBed != 0 && counterBed > 0) {
            counterBed--
            binding.tvCountBed.text = counterBed.toString()
        }
    }

    override fun onPlusBedClicked() {
        if (counterBed < 99) {
            counterBed++
            binding.tvCountBed.text = counterBed.toString()
        }
    }

    override fun onMinusBathClicked() {
        if (counterBath != 0 && counterBath > 0) {
            counterBath--
            binding.tvCountBath.text = counterBath.toString()
        }
    }

    override fun onPlusBathClicked() {
        if (counterBath < 99) {
            counterBath++
            binding.tvCountBath.text = counterBath.toString()
        }
    }

    override fun onMinusCarClicked() {
        if (counterCar != 0 && counterCar > 0) {
            counterCar--
            binding.tvCountCar.text = counterCar.toString()
        }
    }

    override fun onPlusCarClicked() {
        if (counterCar < 99) {
            counterCar++
            binding.tvCountCar.text = counterCar.toString()
        }
    }
    /*Counter clicks end*/

    override fun setObservers() {
    }

    private fun showImageChooserDialog() {
        AlertDialogUtils.showUploadImage(
            this,
            "Choose photo from",
            { dialog, which -> openCamera() }
        ) { dialog, which -> openGallery() }
    }

    private fun openGallery() {


        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(
            ImagePickerActivity.INTENT_ASPECT_RATIO_X,
            binding.clChoosePhoto.measuredWidth.toInt()
        ) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(
            ImagePickerActivity.INTENT_ASPECT_RATIO_Y,
            binding.clChoosePhoto.measuredHeight.toInt()
        )
        resultLauncher.launch(intent)
    }

    private fun openCamera() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(
            ImagePickerActivity.INTENT_ASPECT_RATIO_X,
            binding.clChoosePhoto.measuredWidth.toInt()
        ) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(
            ImagePickerActivity.INTENT_ASPECT_RATIO_Y,
            binding.clChoosePhoto.measuredHeight.toInt()
        )
        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, false)
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_CANCELED) {
                return@registerForActivityResult
            }

            if (result.resultCode == RESULT_OK) {
                val profileUri = result.data!!.getParcelableExtra<Uri>("path")
                profileUri?.let { uri ->
                    //Hide the views of camera & gallery chooser
                    binding.clGalleryCamera.visibility = View.GONE

                    //Shows the imageView and textView of tap to change photo
                    binding.cvPropertyPhoto.visibility = View.VISIBLE
                    binding.tvTapToChangePhoto.visibility = View.VISIBLE
                    propertyPhoto = File(uri.path!!)
                    addNonListedVM.mldPropertyImage.value =
                        File(uri.path!!)    //Add selected into live data variable

                    Glide.with(this).load(uri).into(binding.ivPropertyImage)
                }
            }

        }

    //Address validation
    fun isValidAddress(): Boolean {
        val address: String = binding.etAddress.text.toString().trim()

        if (TextUtils.isEmpty(address)) {
            binding.tvValAddress.text = resources.getString(R.string.msg_empty_address)
            return false
        }

        if (address.length < 2) {
            binding.tvValAddress.text = resources.getString(R.string.msg_length_address)
            return false
        }

        if (GlobalMethods.isContainEmoji(address)) {
            binding.tvValAddress.text =
                resources.getString(R.string.msg_address_not_contain_emoji)
            return false
        }

        return true
    }

    //Land Size validation
    fun isValidLandSize(): Boolean {
        val landSize: String = binding.etLandSize.text.toString().trim()

        if (TextUtils.isEmpty(landSize)) {
            binding.tvValLandSize.text = resources.getString(R.string.msg_empty_last_name)
            return false
        }
        return true
    }

    //Description validation
    fun isValidDescription(): Boolean {
        val description: String = binding.etDescription.text.toString().trim()

        if (TextUtils.isEmpty(description)) {
            binding.tvValDescription.text = resources.getString(R.string.msg_empty_description)
            return false
        }

        if (description.length < 4) {
            binding.tvValDescription.text = resources.getString(R.string.msg_length_description)
            return false
        }
        return true
    }

    //FirsName validation
    fun isValidFirstName(): Boolean {
        val fName: String = binding.etFirstName.text.toString().trim()

        if (TextUtils.isEmpty(fName)) {
            binding.tvValFirstName.text = resources.getString(R.string.msg_empty_first_name)
            return false
        }

        if (fName.length < 2) {
            binding.tvValFirstName.text = resources.getString(R.string.msg_first_name_length)
            return false
        }
        return true
    }

    //LastName validation
    fun isValidLastName(): Boolean {
        val lName: String = binding.etLastName.text.toString().trim()

        if (lName.isEmpty()) {
            binding.tvValLastName.text = resources.getString(R.string.msg_empty_last_name)
            return false
        }

        if (lName.length < 2) {
            binding.tvValLastName.text = resources.getString(R.string.msg_last_name_length)
            return false
        }
        return true
    }

    //Email validation
    private fun isValidEmail(): Boolean {
        val email: String = binding.etEmail.text.toString().trim()

        if (!GlobalMethods.isValidEmail(email)) {
            binding.tvValEmail.text = resources.getString(R.string.msg_invalid_email)
            return false
        }
        return true
    }

    //Phone validation
    private fun isValidPhone(): Boolean {
        val phone: String = binding.etPhone.text.toString().trim()

        if (phone.isNotEmpty() && phone.length < 10) {
            binding.tvValPhone.text = resources.getString(R.string.msg_phone_length_min_10_digits)
            return false
        }
        return true
    }

    //Phone length validation
    private fun isValidPhoneLength(): Boolean {
        val phone: String = binding.etPhone.text.toString().trim()

        if (phone.length < 10) {
            return false
        }
        return true
    }

    //ProfilePic validation
    private fun isValidPropertyPhoto(): Boolean {
        if (propertyPhoto == null) {
            KeyboardUtils.hideKeyboard(this, snackBar)
            AlertDialogUtils.showAlert(this, "Please upload property photo.")
            return false
        }
        return true
    }
}