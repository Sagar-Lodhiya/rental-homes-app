package com.rentalhomes.ui.agent.recommend

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rentalhomes.R
import com.rentalhomes.data.network.model.responseModel.GetRecommendedAgentResponse
import com.rentalhomes.databinding.RowRecommendBinding
import com.rentalhomes.utils.GlobalMethods

class RecommendAdapter(
    private var context: Context,
    private var recommendList: ArrayList<GetRecommendedAgentResponse.Data>,
    private var recommendNavigator: RecommendNavigator
) : RecyclerView.Adapter<RecommendAdapter.MyViewHolder>() {

    fun updateList(recommendList: ArrayList<GetRecommendedAgentResponse.Data>) {
        this.recommendList = recommendList
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: RowRecommendBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding: RowRecommendBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowRecommendBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_recommend,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listItem = recommendList[position]
        Glide.with(context).load(listItem.buyerImage).dontAnimate()
            .placeholder(R.drawable.ic_profile_place_holder).into(holder.binding.ivProfile)
        holder.binding.tvName.text = listItem.buyerName
        holder.binding.tvMobile.text = "+61 ${listItem.mobile}"
        val date: String =
            GlobalMethods.changeDateFormat(listItem.date, "yyyy-MM-dd", "dd MMM yyyy")
        holder.binding.tvDate.text = date

        holder.binding.ivProfile.setOnClickListener {
            if (listItem.userId != 0)
                listItem.userId?.let { recommendNavigator.onItemClick(it) }
        }

        holder.binding.tvName.setOnClickListener {
            if (listItem.userId != 0)
                listItem.userId?.let { recommendNavigator.onItemClick(it) }
        }

    }

    override fun getItemCount(): Int {
        return recommendList.count()
    }
}