package com.rentalhomes.ui.buyer.createtemplate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.model.requestModel.CreateCustomTemplateRequest
import com.rentalhomes.data.network.model.responseModel.AddItemsInTemplateResponse
import com.rentalhomes.data.network.model.responseModel.GetTier3
import com.rentalhomes.databinding.ActivityCreateTemplateBinding
import com.rentalhomes.databinding.BottomsheetAddItemBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.GlobalMethods
import com.rentalhomes.utils.KeyboardUtils

/**
 * Created by Dharmesh
 * */

class CreateTemplateActivity : BaseActivity<ActivityCreateTemplateBinding, CreateTemplateVM>(),
    CreateTemplateNavigator, View.OnClickListener {

    private lateinit var binding: ActivityCreateTemplateBinding
    private lateinit var createTemplateVM: CreateTemplateVM
    private lateinit var snackBar: View

    private var templateList: ArrayList<GetTier3.Data>? = null
    private lateinit var templateCategoryAdapter: TemplateCategoryAdapter

    private var isSetAsDefault: Boolean = false

    //BottomSheet
    private lateinit var bindAddItem: BottomsheetAddItemBinding
    private lateinit var addItemBottomSheet: BottomSheetDialog
    private var itemList: ArrayList<String> = ArrayList()
    private lateinit var addItemAdapter: AddItemAdapter

    private var propertyId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setHeader()
        setOnListener()
//        setTemplateModel()

        val bundle = intent.extras
        propertyId = bundle?.getInt(Keys.PROPERTY_ID)!!

        getTemplates(propertyId)
    }

    private fun init() {
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)
        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this
            it.createTemplateVM = createTemplateVM
        }
        createTemplateVM.attachContext(this)
        createTemplateVM.setNavigator(this)
        snackBar = findViewById(android.R.id.content)
    }

    override val bindingVariable: Int
        get() = BR.createTemplateVM
    override val layoutId: Int
        get() = R.layout.activity_create_template
    override val viewModel: CreateTemplateVM
        get() {
            createTemplateVM = ViewModelProvider(this).get(CreateTemplateVM::class.java)
            return createTemplateVM
        }

    override fun setHeader() {
        binding.headerBar.tvTitle.text = getString(R.string.create_template)
    }

    private fun setOnListener() {
        binding.headerBar.ivBack.setOnClickListener(this)
        binding.btnSave.setOnClickListener {
            val customTempData = ArrayList<CreateCustomTemplateRequest.Data>()

            for (i in templateList!!.indices) {
                for (j in templateList!![i].categoryList!!.indices) {
//                    if (templateList!![i].categoryList!![j].visible == 0) {
                    customTempData.add(
                        CreateCustomTemplateRequest.Data(
                            templateList!![i].categoryList!![j].featureId,
                            templateList!![i].categoryList!![j].visible
                        )
                    )
//                    }
                }
            }
            createTemplateVM.createCustomTemplate(propertyId, customTempData)
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

    //Set Template main list and it's item list
    /*private fun setTemplateModel() {
        templateList.clear()

        templateItem1List.add(TemplateModel.TemplateItem("Broadband", false))
        templateItem1List.add(TemplateModel.TemplateItem("Light Fittings", false))
        templateItem1List.add(TemplateModel.TemplateItem("Smoke Alarms", false))
        templateItem1List.add(TemplateModel.TemplateItem("Tv / Power Points", false))
        templateItem1List.add(TemplateModel.TemplateItem("Security devices", false))
        templateList.add(TemplateModel("Amenities", false, templateItem1List))

        templateItem2List.add(TemplateModel.TemplateItem("Broadband", false))
        templateItem2List.add(TemplateModel.TemplateItem("Light Fittings", false))
        templateItem2List.add(TemplateModel.TemplateItem("Smoke Alarms", false))
        templateItem2List.add(TemplateModel.TemplateItem("Tv / Power Points", false))
        templateItem2List.add(TemplateModel.TemplateItem("Security devices", false))
        templateList.add(TemplateModel("Building", false, templateItem2List))

        templateItem3List.add(TemplateModel.TemplateItem("Broadband", false))
        templateItem3List.add(TemplateModel.TemplateItem("Light Fittings", false))
        templateItem3List.add(TemplateModel.TemplateItem("Smoke Alarms", false))
        templateItem3List.add(TemplateModel.TemplateItem("Tv / Power Points", false))
        templateItem3List.add(TemplateModel.TemplateItem("Security devices", false))
        templateList.add(TemplateModel("Electrical", false, templateItem3List))

        templateItem4List.add(TemplateModel.TemplateItem("Broadband", false))
        templateItem4List.add(TemplateModel.TemplateItem("Light Fittings", false))
        templateItem4List.add(TemplateModel.TemplateItem("Smoke Alarms", false))
        templateItem4List.add(TemplateModel.TemplateItem("Tv / Power Points", false))
        templateItem4List.add(TemplateModel.TemplateItem("Security devices", false))
        templateList.add(TemplateModel("External", false, templateItem4List))
    }*/

    override fun setObservers() {
    }

    override fun onAddItemClicked(position: Int, categoryId: Int) {
        showAddItemBottomSheet(position, categoryId)
    }

    override fun onSetAsDefaultClicked() {
        if (!isSetAsDefault) {
            isSetAsDefault = true
            binding.rbSetAsDefault.isChecked = true
        } else {
            isSetAsDefault = false
            binding.rbSetAsDefault.isChecked = false
        }
    }

    private var categoryId: Int = 0

    //Add item BottomSheet
    private fun showAddItemBottomSheet(position: Int, categoryId: Int) {
        //Create instance of BottomSheetDialog
        this.categoryId = categoryId
        addItemBottomSheet = BottomSheetDialog(this)

        /*Binding BottomSheet start*/
        bindAddItem = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.bottomsheet_add_item,
            null,
            false
        )
        addItemBottomSheet.setContentView(bindAddItem.root)
        /*binding end*/

        //Open in full size
//        addItemBottomSheet.behavior.state = STATE_EXPANDED

        /*set Add Item RecyclerView*/
//        addItemList.clear()
        itemList.clear()
        addItemAdapter = AddItemAdapter(this, itemList, this)
//        bindAddItem.rvCustomTemplate.itemAnimator = null  //Uncomment this line if animation needs on changes of items
        bindAddItem.rvCustomTemplate.apply {
            this.adapter = addItemAdapter
        }

        //Item name EditText TextWatcher
        bindAddItem.etItemName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.isNotEmpty()) {
                    bindAddItem.tvAddItem.visibility = View.VISIBLE
                } else {
                    bindAddItem.tvAddItem.visibility = View.GONE
                }
            }

        })

        //BottomSheet Add Item button click
        bindAddItem.tvAddItem.setOnClickListener {
//            addItemList.add(TemplateModel.AddItem(bindAddItem.etItemName.text.toString().trim()))
            itemList.add(bindAddItem.etItemName.text.toString().trim())
            addItemAdapter.notifyItemChanged(position)
            bindAddItem.etItemName.setText("")

            if (itemList.count() > 4) {
                bindAddItem.etItemName.visibility = View.GONE
                bindAddItem.tvAddItem.visibility = View.GONE
            } else {
                bindAddItem.etItemName.visibility = View.VISIBLE
                bindAddItem.tvAddItem.visibility = View.VISIBLE
            }
        }

        //BottomSheet Save button click
        bindAddItem.btnSave.setOnClickListener {
            viewModel.addItemsInTemplate(categoryId, itemList)
        }

        //BottomSheet Cancel button click
        bindAddItem.btnCancel.setOnClickListener {
            addItemBottomSheet.dismiss()
        }

        addItemBottomSheet.show()
    }

    override fun onAfterDeletedItem(position: Int) {
        if (addItemAdapter.itemCount != 5) {
            bindAddItem.etItemName.visibility = View.VISIBLE
            bindAddItem.tvAddItem.visibility = View.VISIBLE
        } else {
            bindAddItem.etItemName.visibility = View.GONE
            bindAddItem.tvAddItem.visibility = View.GONE
        }
    }

    override fun showProgress() {
        showProgressDialog()
    }

    override fun hideProgress() {
        hideProgressDialog()
    }

    override fun setMessageComingFromServer(message: String?) {
        AlertDialogUtils.showToast(this, message)
    }

    override fun addTemplatesToCategory(obj: Any) {
        val template: AddItemsInTemplateResponse = obj as AddItemsInTemplateResponse
        addItemBottomSheet.dismiss()

        if (template.data != null && template.data!!.isNotEmpty()) {
            Log.e("RESPONSE", Gson().toJson(template.data))
        }
        getTemplates(propertyId)
    }

    private fun getTemplates(propertyId: Int) {
        viewModel.getTier3Data(propertyId)

        viewModel.tier3Data.observe(this) { tier3 ->
//          Tier 3 set categories
            tier3.data.let { tierData ->
                templateList = tierData!!
                templateCategoryAdapter = TemplateCategoryAdapter(this, templateList!!, this, -1)
                binding.rvTemplateCategory.apply {
                    this.adapter = templateCategoryAdapter
                }
            }
        }
    }

    override fun onCustomTemplateSuccess() {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}