package com.rentalhomes.ui.buyer.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rentalhomes.R
import com.rentalhomes.data.network.model.responseModel.GetPropertyListBuyerResponse
import com.rentalhomes.databinding.RowBuyerHomeListBinding
import com.rentalhomes.utils.GlobalMethods

class BuyerHomeAdapter(
    private var context: Context,
    private var propertyList: ArrayList<GetPropertyListBuyerResponse.Data>,
    private var homeNavigator: BuyerHomeNavigator,
    var isCompareMode: Boolean
) : RecyclerView.Adapter<BuyerHomeAdapter.MyViewHolder>() {

    fun updateList(propertyList: ArrayList<GetPropertyListBuyerResponse.Data>) {
        this.propertyList = propertyList
        notifyDataSetChanged()
    }

    private var tickCount: Int = 0

    class MyViewHolder(itemView: RowBuyerHomeListBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding: RowBuyerHomeListBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowBuyerHomeListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_buyer_home_list,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listItem = propertyList[position]
        Glide.with(context).load(listItem.propertyImage).dontAnimate()
            .placeholder(R.drawable.ic_proparty_listing_placeholder)
            .into(holder.binding.ivProperty)
        Glide.with(context).load(listItem.agentDetails?.agentProfilePic).dontAnimate()
            .placeholder(R.drawable.ic_profile_place_holder)
            .into(holder.binding.ivAgentProfile)
        holder.binding.tvAddress.text = listItem.propertyAddress!!.replace("\n", " ")
        holder.binding.tvCity.text = listItem.propertyCity!!.replace("\n", " ")
        val date: String = GlobalMethods.changeDateFormat(
            listItem.propertyCreatedDate,
            "yyyy-MM-dd",
            "dd MMM yyyy"
        )
        holder.binding.tvDate.text = date
        holder.binding.tvAgentName.text = listItem.agentDetails?.agentName

        when (listItem.ratingScore) {
            0 -> {
                holder.binding.ivPropertyRating.setImageResource(R.drawable.ic_zero)
            }
            1 -> {
                holder.binding.ivPropertyRating.setImageResource(R.drawable.ic_low)
            }
            3 -> {
                holder.binding.ivPropertyRating.setImageResource(R.drawable.ic_meets)
            }
            5 -> {
                holder.binding.ivPropertyRating.setImageResource(R.drawable.ic_higher)
            }
            6 -> {
                holder.binding.ivPropertyRating.setImageResource(R.drawable.ic_exceeds)
            }
        }

//        holder.binding.clMain.setOnClickListener {
//            if (!isCompareMode)
//                homeNavigator.onItemClick()
//        }

        if (isCompareMode && propertyList[position].isTick == 1) {
            holder.binding.ivMark.visibility = View.VISIBLE
        } else {
            holder.binding.ivMark.visibility = View.INVISIBLE
        }

        if (propertyList[position].propertyType == 1) {
            holder.binding.clEditProperty.visibility = View.VISIBLE
            holder.binding.ivAgentProfile.visibility = View.GONE
            holder.binding.tvAgentName.visibility = View.GONE
        } else {
            holder.binding.clEditProperty.visibility = View.GONE
            holder.binding.ivAgentProfile.visibility = View.VISIBLE
            holder.binding.tvAgentName.visibility = View.VISIBLE
        }

        //Below condition commented to show only Tick icon, It's condition checked in above code
        //Tick function
//        if (propertyList[position].isTick) {
//            holder.binding.ivMark.setImageResource(R.drawable.ic_compare_selected_icon)
//        } else {
//            holder.binding.ivMark.setImageResource(R.drawable.ic_compare_unselected_icon)
//        }

        holder.binding.clMain.setOnClickListener {
            if (propertyList[position].isTick == 0) {
                if (tickCount < 3) {
                    propertyList[position].isTick = 1
                }
                notifyItemChanged(position)
            } else {
                propertyList[position].isTick = 0
                notifyItemChanged(position)
            }

            tickCount = 0
            for (i in 0 until propertyList.size) {
                if (propertyList[i].isTick == 1) {
                    tickCount++
                }
            }

            if (!isCompareMode)
                homeNavigator.onItemClick(propertyList[position])

            if (tickCount == 0) {
                homeNavigator.onTickCountZero()
            }

        }

        //Delete Property
        holder.binding.clDeleteProperty.setOnClickListener {
            homeNavigator.onDeletePropertyClick(listItem)
        }

        //Edit Property
        holder.binding.clEditProperty.setOnClickListener {
            homeNavigator.onEditPropertyClick(listItem)
        }
    }

    override fun getItemCount(): Int {
        return propertyList.count()
    }
}