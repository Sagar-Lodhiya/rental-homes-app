package com.rentalhomes.ui.buyer.createtemplate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rentalhomes.R
import com.rentalhomes.data.network.model.responseModel.GetTier3
import com.rentalhomes.databinding.RowTemplateCategoryBinding

class TemplateCategoryAdapter(
    private val context: Context,
    private var templateList: ArrayList<GetTier3.Data>,
    private var navigator: CreateTemplateNavigator,
    private var selected: Int,
) :
    RecyclerView.Adapter<TemplateCategoryAdapter.MyViewHolder>() {
    private lateinit var templateItemAdapter: TemplateItemAdapter
    private var isOpen = false

    class MyViewHolder(itemView: RowTemplateCategoryBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowTemplateCategoryBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowTemplateCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_template_category,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvTemplateCategory.text = templateList[position].category

        //Click to Expand child list
        holder.binding.clTemplateCategory.tag = position
        holder.binding.clTemplateCategory.setOnClickListener { v ->
            if (!isOpen) {
                isOpen = true
                selected = v.tag as Int
            } else {
                if (selected == v.tag as Int) {
                    isOpen = false
                    selected = -1
                } else {
                    isOpen = false
                    selected = -1
                    isOpen = true
                    selected = v.tag as Int
                }
            }
            notifyDataSetChanged()
        }

        //Expand and collapsed recyclerView here
        if (position == selected) {
            //Visible item's recyclerView
            holder.binding.rvTemplateItem.visibility = View.VISIBLE
            holder.binding.vdUnderTitle.visibility = View.VISIBLE
            //Visible add items button
            holder.binding.clAddItems.visibility = View.VISIBLE
            //Change color of Template name to green
            holder.binding.tvTemplateCategory.setTextColor(context.getColor(R.color.color_green))
            //Rotate arrow up with anti-clock wise rotation
            holder.binding.ivDownArrow.animate().setDuration(100).rotation(180F)
        } else {
            //Hide item's recyclerView
            holder.binding.rvTemplateItem.visibility = View.GONE
            holder.binding.vdUnderTitle.visibility = View.GONE
            //Hide add items button
            holder.binding.clAddItems.visibility = View.GONE
            //Change color of Template name to black
            holder.binding.tvTemplateCategory.setTextColor(context.getColor(R.color.black))
            //Rotate arrow up with clock wise rotation
            holder.binding.ivDownArrow.animate().setDuration(100).rotation(0F)
        }

        //Click of Add Items to open bottom sheet
        holder.binding.clAddItems.setOnClickListener {
            navigator.onAddItemClicked(position, templateList[position].categoryId)
        }

        //Set child RecyclerView
        templateList[position].categoryList?.let { categoryList ->
            templateItemAdapter = TemplateItemAdapter(context, categoryList, navigator)
            holder.binding.rvTemplateItem.apply {
                this.adapter = templateItemAdapter
            }
        }
    }

    override fun getItemCount(): Int {
        return templateList.count()
    }
}