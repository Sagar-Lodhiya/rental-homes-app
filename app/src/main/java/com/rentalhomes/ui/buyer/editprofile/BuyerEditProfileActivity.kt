package com.rentalhomes.ui.buyer.editprofile

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.R
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.data.pref.AppSessionManager.Companion.PREF_KEY_PROFILE_IMAGE
import com.rentalhomes.databinding.ActivityBuyerEditProfileBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.utils.*
import java.io.File



class BuyerEditProfileActivity :
    BaseActivity<ActivityBuyerEditProfileBinding, BuyerEditProfileVM>(),
    BuyerEditProfileNavigator {
    private lateinit var binding: ActivityBuyerEditProfileBinding
    private lateinit var buyerEditProfileVM: BuyerEditProfileVM
    private lateinit var snackBar: View

    private var profilePic: File? = null
    private var permissions = arrayOf<String>(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    private var CAMERA_READ_WRITE_PERMISSION_REQUEST_CODE = 101
    private var GALLERY_VIEW_PERMISSION = 102
    private var REQUEST_CODE_AUTOCOMPLETE: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setOnListener()
        setHeader()
//        setDefaults()
        setValuesFromPref()
        KeyboardUtils.hideKeyboard(this, snackBar)
        binding.etEmail.isEnabled = false
        checkEmptyPic()
    }

    private fun checkEmptyPic() {
        if (profilePic != null || RentalHomesApp.appSessionManager.getString(
                this,
                AppSessionManager.PREF_NAME,
                AppSessionManager.PREF_KEY_PROFILE_IMAGE
            ) != ""
        ) {
            binding.tvOr.visibility = View.VISIBLE
            binding.tvRemove.visibility = View.VISIBLE
        } else {
            binding.tvRemove.visibility = View.GONE
            binding.tvOr.visibility = View.GONE
        }
    }

    private fun init() {
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)
        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this
            it.buyerEditProfileVM = buyerEditProfileVM
        }
        buyerEditProfileVM.attachContext(this)
        buyerEditProfileVM.setNavigator(this)
        snackBar = findViewById(android.R.id.content)
    }

    override val bindingVariable: Int
        get() = BR.buyerEditProfileVM

    override val layoutId: Int
        get() = R.layout.activity_buyer_edit_profile
    override val viewModel: BuyerEditProfileVM
        get() {
            buyerEditProfileVM = ViewModelProvider(this).get(BuyerEditProfileVM::class.java)
            return buyerEditProfileVM
        }

    private fun setDefaults() {
        buyerEditProfileVM.profilePic.value = null
        Glide.with(this).load(intent.getStringExtra(PREF_KEY_PROFILE_IMAGE))
            .into(binding.civProfile)
        binding.etFirstName.setText(intent.getStringExtra(AppSessionManager.PREF_KEY_FIRST_NAME))
        binding.etLastName.setText(intent.getStringExtra(AppSessionManager.PREF_KEY_LAST_NAME))
        binding.etEmail.setText(intent.getStringExtra(AppSessionManager.PREF_KEY_EMAIL))
        binding.etMobile.setText(intent.getStringExtra(AppSessionManager.PREF_KEY_MOBILE))
        binding.etSuburb.setText(intent.getStringExtra(AppSessionManager.PREF_KEY_POST_CODE))
    }

    private fun setValuesFromPref() {
        val thumbnailRequest = Glide.with(this)
            .load(
                RentalHomesApp.appSessionManager.getString(
                    this,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_PROFILE_THUMB
                )
            )

        Glide.with(this)
            .load(
                RentalHomesApp.appSessionManager.getString(
                    this,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_PROFILE_IMAGE
                )
            )
            .thumbnail(thumbnailRequest)
            .placeholder(R.drawable.ic_profile_place_holder)
            .into(binding.civProfile)

        binding.etFirstName.setText(
            RentalHomesApp.appSessionManager.getString(
                this,
                AppSessionManager.PREF_NAME,
                AppSessionManager.PREF_KEY_FIRST_NAME
            )
        )

        binding.etLastName.setText(
            RentalHomesApp.appSessionManager.getString(
                this,
                AppSessionManager.PREF_NAME,
                AppSessionManager.PREF_KEY_LAST_NAME
            )
        )

        binding.etEmail.setText(
            RentalHomesApp.appSessionManager.getString(
                this,
                AppSessionManager.PREF_NAME,
                AppSessionManager.PREF_KEY_EMAIL
            )
        )

        binding.etMobile.setText(
            RentalHomesApp.appSessionManager.getString(
                this,
                AppSessionManager.PREF_NAME,
                AppSessionManager.PREF_KEY_MOBILE
            )
        )

        binding.etSuburb.setText(
            RentalHomesApp.appSessionManager.getString(
                this,
                AppSessionManager.PREF_NAME,
                AppSessionManager.PREF_KEY_SUBURB
            )
        )
    }

    override fun setObservers() {
    }

    override fun setHeader() {
        binding.headerBar.tvTitle.text = getString(R.string.edit_profile)
        binding.headerBar.ivBack.setOnClickListener {
            KeyboardUtils.hideKeyboard(this, snackBar)
            onBackPressed()
        }
    }

    private fun setOnListener() {
        //Pin Code FocusChangeListener
        /*binding.etPin.onFocusChangeListener =
            View.OnFocusChangeListener { view, hashFocus ->
                if (hashFocus) {
                    binding.tvPin.setTextColor(getColor(R.color.color_green))
                } else {
                    binding.tvPin.setTextColor(getColor(R.color.grey_100))
                }
            }*/

        //First Name FocusChangeListener
        binding.etFirstName.onFocusChangeListener =
            View.OnFocusChangeListener { view, hashFocus ->
                if (hashFocus) {
                    binding.tvFirstName.setTextColor(getColor(R.color.color_green))
                } else {
                    binding.tvFirstName.setTextColor(getColor(R.color.grey_100))
                }
            }

        //Last Name FocusChangeListener
        binding.etLastName.onFocusChangeListener =
            View.OnFocusChangeListener { view, hashFocus ->
                if (hashFocus) {
                    binding.tvLastName.setTextColor(getColor(R.color.color_green))
                } else {
                    binding.tvLastName.setTextColor(getColor(R.color.grey_100))
                }
            }

        //Email FocusChangeListener
        binding.etEmail.onFocusChangeListener =
            View.OnFocusChangeListener { view, hashFocus ->
                if (hashFocus) {
                    binding.tvEmail.setTextColor(getColor(R.color.color_green))
                } else {
                    binding.tvEmail.setTextColor(getColor(R.color.grey_100))
                }
            }

        //Mobile FocusChangeListener
        binding.etMobile.onFocusChangeListener =
            View.OnFocusChangeListener { view, hashFocus ->
                if (hashFocus) {
                    binding.tvMobile.setTextColor(getColor(R.color.color_green))
                } else {
                    binding.tvMobile.setTextColor(getColor(R.color.grey_100))
                }
            }

        //Agency Name TextWatcher
        /*binding.etPin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Check and show validation of First Name
                if (!isValidPostCode() && p0.toString().isNotEmpty()) {
                    binding.tvValPin.visibility = View.VISIBLE
                } else {
                    binding.tvValPin.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (isValidPostCode() && isValidFirstName() && isValidLastName() && isValidEmail() && isValidPhoneLength()) {
                    GlobalMethods.enableButton(this@BuyerEditProfileActivity, binding.btnSave)
                } else {
                    GlobalMethods.disableButton(this@BuyerEditProfileActivity, binding.btnSave)
                }
            }

        })*/

        //Agency Name TextWatcher
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
                if (isValidSuburb() && isValidFirstName() && isValidLastName() && isValidEmail() && isValidPhoneLength()) {
                    GlobalMethods.enableButton(this@BuyerEditProfileActivity, binding.btnSave)
                } else {
                    GlobalMethods.disableButton(this@BuyerEditProfileActivity, binding.btnSave)
                }
            }

        })

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
                if (isValidSuburb() && isValidFirstName() && isValidLastName() && isValidEmail() && isValidPhoneLength()) {
                    GlobalMethods.enableButton(this@BuyerEditProfileActivity, binding.btnSave)
                } else {
                    GlobalMethods.disableButton(this@BuyerEditProfileActivity, binding.btnSave)
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
                if (isValidSuburb() && isValidFirstName() && isValidLastName() && isValidEmail() && isValidPhoneLength()) {
                    GlobalMethods.enableButton(this@BuyerEditProfileActivity, binding.btnSave)
                } else {
                    GlobalMethods.disableButton(this@BuyerEditProfileActivity, binding.btnSave)
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
                if (isValidSuburb() && isValidFirstName() && isValidLastName() && isValidEmail() && isValidPhoneLength()) {
                    GlobalMethods.enableButton(this@BuyerEditProfileActivity, binding.btnSave)
                } else {
                    GlobalMethods.disableButton(this@BuyerEditProfileActivity, binding.btnSave)
                }
            }

        })

        //Mobile TextWatcher
        binding.etMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Check and show validation of First Name
                if (!isValidPhone() && p0.toString().isNotEmpty()) {
                    binding.tvValMobile.visibility = View.VISIBLE
                } else {
                    binding.tvValMobile.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (isValidSuburb() && isValidFirstName() && isValidLastName() && isValidEmail() && isValidPhone()) {
                    GlobalMethods.enableButton(this@BuyerEditProfileActivity, binding.btnSave)
                } else {
                    GlobalMethods.disableButton(this@BuyerEditProfileActivity, binding.btnSave)
                }
            }

        })

        //Suburb click
        binding.etSuburb.setOnClickListener {
            openPlacePicker()
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

    //Pin Code validation
    /*fun isValidPostCode(): Boolean {
        val postCode: String = binding.etPin.text.toString().trim()

        if (TextUtils.isEmpty(postCode)) {
            binding.tvValPin.text = resources.getString(R.string.msg_empty_pin_code)
            return false
        }

        if (postCode.length < 6) {
            binding.tvValPin.text = resources.getString(R.string.msg_pin_code_length)
            return false
        }
        return true
    }*/

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
        val phone: String = binding.etMobile.text.toString().trim()

        if (phone.isNotEmpty() && phone.length < 9) {
            binding.tvValMobile.text = resources.getString(R.string.msg_phone_length_min_10_digits)
            return false
        }
        return true
    }

    //Phone length validation
    private fun isValidPhoneLength(): Boolean {
        val phone: String = binding.etMobile.text.toString().trim()

        if (phone.length < 9) {
            return false
        }
        return true
    }

    //ProfilePic validation
    private fun isValidProfilePic(): Boolean {
        if (profilePic == null) {
            KeyboardUtils.hideKeyboard(this, snackBar)
            AlertDialogUtils.showAlert(this, "Please upload your profile pic.")
            return false
        }
        return true
    }

    override fun onSaveClicked() {
        if (isValidProfilePic()) {
            finish()
        }
    }

    override fun onUpdateClicked() {
        showImageChooserDialog()
    }

    override fun onRemoveClicked() {
        if (profilePic != null || RentalHomesApp.appSessionManager.getString(
                this,
                AppSessionManager.PREF_NAME,
                AppSessionManager.PREF_KEY_PROFILE_IMAGE
            ) != ""
        ) {
            AlertDialogUtils.showConfirmAlert(
                this,
                "Are you sure you want to remove your profile pic?"
            ) { dialog, _ ->
                dialog.dismiss()
                Glide.with(this).load("").placeholder(R.drawable.ic_profile_place_holder)
                    .into(binding.civProfile)
                profilePic = null
                RentalHomesApp.appSessionManager.setString(
                    this,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_PROFILE_IMAGE,
                    ""
                )
                checkEmptyPic()
                buyerEditProfileVM.isProfileRemove.value = 1
            }
        } else {
            AlertDialogUtils.showAlert(this, "Please upload profile picture first.")
        }
    }

    override fun isValid(): Boolean {
        if (profilePic == null) {
            KeyboardUtils.hideKeyboard(this, snackBar)
            AlertDialogUtils.showAlert(this, "Please upload your profile pic.")
            return false
        }
        return true
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

    override fun onEditProfileComplete(message: String?) {
        AlertDialogUtils.showSnakeBar(message, snackBar, this)
        finish()
    }

    private fun showImageChooserDialog() {
        AlertDialogUtils.showUploadImage(
            this,
            "Choose photo from",
            { dialog, which -> capturePhoto() }
        ) { dialog, which -> browseGallery() }
    }

    private fun capturePhoto() {
        var permissionGranted = PermissionHelper.arePermissionsGranted(
            this, permissions
        )

        if (permissionGranted) {
            openCamera()
        } else {
            PermissionHelper.askUserForPermission(
                this,
                permissions,
                CAMERA_READ_WRITE_PERMISSION_REQUEST_CODE,
                AppSessionManager.PREF_CAMERA_AND_READ_WRITE_PERMISSION_ASKED,
                getString(R.string.camera_and_storage_permission_needed)
            )
        }
    }

    private fun browseGallery() {
        var permissionGranted = PermissionHelper.arePermissionsGranted(
            this, permissions
        )

        if (permissionGranted) {
            openGallery()
        } else {
            PermissionHelper.askUserForPermission(
                this,
                permissions,
                GALLERY_VIEW_PERMISSION,
                AppSessionManager.PREF_CAMERA_AND_READ_WRITE_PERMISSION_ASKED,
                getString(R.string.camera_and_storage_permission_needed)
            )
        }
    }

    private fun openGallery() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )
        //        intent for making crop circular
        intent.putExtra(ImagePickerActivity.INTENT_IS_CIRCULAR, true)
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1) // 16x9, 1x1, 3:4, 3:2
        resultLauncher.launch(intent)
    }

    private fun openCamera() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )
        //        intent for making crop circular
        intent.putExtra(ImagePickerActivity.INTENT_IS_CIRCULAR, true)
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, false)
        resultLauncher.launch(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_READ_WRITE_PERMISSION_REQUEST_CODE) {
            if (PermissionHelper.arePermissionsGranted(
                    this, permissions as Array<String>
                )
            ) {
                openCamera()
            }
        } else if (requestCode == GALLERY_VIEW_PERMISSION) {
            if (PermissionHelper.arePermissionsGranted(
                    this, permissions as Array<String>
                )
            ) {
                openGallery()
            }
        }

    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_CANCELED) {
                return@registerForActivityResult
            }

            if (result.resultCode == RESULT_OK) {
                val profileUri = result.data!!.getParcelableExtra<Uri>("path")
                profileUri?.let { uri ->
                    profilePic = File(uri.path)
                    buyerEditProfileVM.profilePic.value =
                        File(uri.path)    //Add into live data variable

                    Glide.with(this).load(uri).into(binding.civProfile)
                    buyerEditProfileVM.isProfileRemove.value = 0
                    checkEmptyPic()
                }
            }

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
            if (resultCode == RESULT_OK) {
                // Get Place data from intent
                val place = Autocomplete.getPlaceFromIntent(data)
                binding.etSuburb.setText(place.name)
                KeyboardUtils.hideKeyboard(this, snackBar)
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data)
                //Log.i("place_address_status", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Toast.makeText(this, "Result got cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}