package com.rentalhomes.ui.common.setting.contactus

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.databinding.ActivityContactUsBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.utils.GlobalMethods

/**
 * Created by Dharmesh
 * */
class ContactUsActivity : BaseActivity<ActivityContactUsBinding, ContactUsVM>(),
    ContactUsNavigator {
    private lateinit var binding: ActivityContactUsBinding
    private lateinit var contactUsVM: ContactUsVM
    private lateinit var snackBar: View

    private val contactUsList = ArrayList<ContactUsModel>()
    private lateinit var contactUsAdapter: ContactUsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setHeader()
        setOnListener()
        setContactUsModel()
        setContactUsRecyclerView()
    }

    //Initialization is done here
    private fun init() {
        //Set status bar color
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)

        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this
            it.contactUsVM = contactUsVM
        }
        contactUsVM.attachContext(this)
        contactUsVM.setNavigator(this)

        //Initialize snackBar object here
        snackBar = findViewById(android.R.id.content)
    }

    //Data binding
    override val bindingVariable: Int
        get() = BR.contactUsVM
    override val layoutId: Int
        get() = R.layout.activity_contact_us
    override val viewModel: ContactUsVM
        get() {
            contactUsVM = ViewModelProvider(this).get(ContactUsVM::class.java)
            return contactUsVM
        }

    private fun setOnListener() {
        binding.headerBar.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onSettingItemClick(position: Int, contactEmail: String) {
        when (position) {
            0 -> {
                sendEmail("info@sagar.com", "General", "General contact info")
            }
            1 -> {
                sendEmail("info@sagar.com", "Marketing", "Marketing contact info")
            }
            2 -> {
                sendEmail("info@sagar.com", "Technical Support", "Technical Support contact info")
            }
        }
    }

    private fun sendEmail(contactEmail: String, subject: String, body: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(contactEmail))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, body)
        intent.type = "message/rfc822"
        intent.setPackage("com.google.android.gm")
        startActivity(Intent.createChooser(intent, "Select email"))
    }

    override fun setHeader() {
        binding.headerBar.tvTitle.text = resources.getString(R.string.contact_us)
    }

    private fun setContactUsRecyclerView() {
        contactUsAdapter = ContactUsAdapter(this, contactUsList, this)
        binding.rvContactUs.apply {
            this.adapter = contactUsAdapter
        }
    }

    private fun setContactUsModel() {
        contactUsList.clear()
        contactUsList.add(
            ContactUsModel(
                getString(R.string.general),
                "info@sagar.com",
                R.drawable.ic_general
            )
        )
        contactUsList.add(
            ContactUsModel(
                getString(R.string.marketing),
                "marketing@sagar.com",
                R.drawable.ic_marketing
            )
        )
        contactUsList.add(
            ContactUsModel(
                getString(R.string.technical_support),
                "techinical@sagar.com",
                R.drawable.ic_technical_support
            )
        )
    }

    override fun isValid(): Boolean {
        return true
    }

    override fun setObservers() {
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }
}