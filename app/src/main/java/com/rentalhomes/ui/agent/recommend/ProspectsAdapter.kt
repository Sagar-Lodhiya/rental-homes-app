package com.rentalhomes.ui.agent.recommend

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rentalhomes.R
import com.rentalhomes.data.network.model.responseModel.GetRecommendedAgentResponse
import com.rentalhomes.databinding.RowProspectsBinding

class ProspectsAdapter(
    private var context: Context,
    private var prospectsList: ArrayList<GetRecommendedAgentResponse.Data>,
    private var prospectNavigator: ProspectNavigator
) : RecyclerView.Adapter<ProspectsAdapter.MyViewHolder>() {

    fun updateList(prospectsList: ArrayList<GetRecommendedAgentResponse.Data>) {
        this.prospectsList = prospectsList
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: RowProspectsBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding: RowProspectsBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowProspectsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_prospects,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listItem = prospectsList[position]
        Glide.with(context).load(listItem.buyerImage)
            .placeholder(R.drawable.ic_profile_place_holder).into(holder.binding.ivProfile)
        holder.binding.tvName.text = listItem.buyerName
        holder.binding.tvMobile.text = "+61 ${listItem.mobile}"
        holder.binding.btnLike.setOnClickListener {
            prospectNavigator.onLikeClick(listItem.userId!!, position)
        }

        holder.binding.ivProfile.setOnClickListener { holder.binding.tvName.performClick() }
        holder.binding.tvName.setOnClickListener {
            if (listItem.userId != 0)
                listItem.userId?.let { it1 -> prospectNavigator.onUserClick(it1) }
        }
    }

    override fun getItemCount(): Int {
        return prospectsList.count()
    }
}