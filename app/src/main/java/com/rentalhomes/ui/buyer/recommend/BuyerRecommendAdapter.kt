package com.rentalhomes.ui.buyer.recommend

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rentalhomes.R
import com.rentalhomes.data.network.model.responseModel.GetRecommendListBuyerResponse
import com.rentalhomes.databinding.RowBuyerRecommendBinding
import com.rentalhomes.utils.GlobalMethods

class BuyerRecommendAdapter(
    private var context: Context,
    private var propertyList: ArrayList<GetRecommendListBuyerResponse.Data>,
    private var homeNavigator: BuyerRecommendNavigator
) : RecyclerView.Adapter<BuyerRecommendAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: RowBuyerRecommendBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding: RowBuyerRecommendBinding = itemView
    }

    fun updateList(propertyList: ArrayList<GetRecommendListBuyerResponse.Data>) {
        this.propertyList = propertyList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowBuyerRecommendBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_buyer_recommend,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listItem = propertyList[position]
        Glide.with(context).load(listItem.propertyImage)
            .placeholder(R.drawable.ic_proparty_listing_placeholder)
            .into(holder.binding.ivProperty)
        Glide.with(context).load(listItem.agentDetails?.agentProfilePic)
            .placeholder(R.drawable.ic_profile_place_holder).into(holder.binding.ivAgentProfile)
//        holder.binding.ivAgentProfile.setImageResource(R.drawable.ic_profile_place_holder)
        holder.binding.tvAddress.text = listItem.propertyAddress!!.replace("\n", " ")
        holder.binding.tvCity.text = listItem.propertyCity!!.replace("\n", " ")
        val date: String = GlobalMethods.changeDateFormat(
            listItem.propertyCreatedDate,
            "yyyy-MM-dd",
            "dd MMM yyyy"
        )
        holder.binding.tvDate.text = date
        holder.binding.tvAgentName.text = listItem.agentDetails?.agentName
        holder.binding.ivMark.visibility = View.GONE

        holder.binding.clMain.setOnClickListener {
            homeNavigator.onItemClick(listItem)
        }

    }

    override fun getItemCount(): Int {
        return propertyList.count()
    }
}