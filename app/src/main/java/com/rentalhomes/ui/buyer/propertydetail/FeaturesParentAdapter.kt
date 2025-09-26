package com.rentalhomes.ui.buyer.propertydetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rentalhomes.R
import com.rentalhomes.data.network.model.responseModel.GetTier3
import com.rentalhomes.databinding.RowFeaturesParentBinding

class FeaturesParentAdapter(
    private val context: Context,
    private var featuresList: ArrayList<GetTier3.Data>,
    private var navigator: BuyerPropertyDetailNavigator,
    private var selected: Int,
) : RecyclerView.Adapter<FeaturesParentAdapter.MyViewHolder>() {
    private var isOpen = false

    class MyViewHolder(itemView: RowFeaturesParentBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowFeaturesParentBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowFeaturesParentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_features_parent,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvGroupName.text = featuresList[position].category

        //Click to Expand child list
//        holder.binding.clGroupName.tag = position
        holder.binding.clGroupName.setOnClickListener { v ->
            /*if (!isOpen) {
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
            }*/

            if (!featuresList[position].expanded) {
                for (i in featuresList.indices) {
                    featuresList[i].expanded = i == position
                }
            } else {
                featuresList[position].expanded = false
            }

            notifyDataSetChanged()
        }

        //Conditions to set visibility of child RecyclerView
//        if (position == selected) {
        if (featuresList[position].expanded) {
            holder.binding.rvFeaturesChild.visibility = View.VISIBLE
            holder.binding.vdUnderTitle.visibility = View.VISIBLE
            //Change color of Template name to green
            holder.binding.tvGroupName.setTextColor(context.getColor(R.color.color_green))
            //Rotate arrow up with anti-clock wise rotation
            holder.binding.ivDownArrow.animate().setDuration(100).rotation(180F)

        } else {
            holder.binding.rvFeaturesChild.visibility = View.GONE
            holder.binding.vdUnderTitle.visibility = View.GONE
            //Change color of Template name to black
            holder.binding.tvGroupName.setTextColor(context.getColor(R.color.black))
            //Rotate arrow up with clock wise rotation
            holder.binding.ivDownArrow.animate().setDuration(100).rotation(0F)
        }

        //Set child RecyclerView
        featuresList[position].categoryList?.let { categoryList ->
            /*val catList = ArrayList<GetTier3.Category>()
            for (i in categoryList.indices) {
                if (categoryList[i].visible == 1) {
                    val cat = GetTier3.Category()
                    cat.featureId = categoryList[i].featureId
                    cat.featureName = categoryList[i].featureName
                    cat.visible = categoryList[i].visible
                    cat.like = categoryList[i].like
                    catList.add(cat)
                }
            }*/

            val featuresChildAdapter = FeaturesChildAdapter(context, categoryList, navigator)
            holder.binding.rvFeaturesChild.apply {
                this.adapter = featuresChildAdapter
            }
        }
    }

    override fun getItemCount(): Int {
        return featuresList.count()
    }

}