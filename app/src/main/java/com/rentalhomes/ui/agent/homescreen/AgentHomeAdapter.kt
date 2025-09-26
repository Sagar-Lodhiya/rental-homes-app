package com.rentalhomes.ui.agent.homescreen

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rentalhomes.R
import com.rentalhomes.data.network.model.responseModel.GetAgentPropertyListResponse
import com.rentalhomes.databinding.RowListedBinding
import com.rentalhomes.utils.GlobalMethods

class AgentHomeAdapter(
    private var context: Context,
    private var propertyList: ArrayList<GetAgentPropertyListResponse.Data>,
    private var homeNavigator: AgentHomeNavigator
) : RecyclerView.Adapter<AgentHomeAdapter.MyViewHolder>() {

    fun updateList(propertyList: ArrayList<GetAgentPropertyListResponse.Data>) {
        this.propertyList = propertyList
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: RowListedBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding: RowListedBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowListedBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_listed,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listItem = propertyList[position]
        if (listItem.propertyImage!!.isEmpty()) {
            holder.binding.ivProperty.setImageResource(R.drawable.ic_proparty_listing_placeholder)
        } else {
            Glide.with(context).load(listItem.propertyImage).dontAnimate()
                .placeholder(R.drawable.ic_proparty_listing_placeholder)
                .error(R.drawable.ic_proparty_listing_placeholder)
                .into(holder.binding.ivProperty)
        }
        holder.binding.tvAddress.text = listItem.propertyAddress!!.replace("\n"," ")
        holder.binding.tvCity.text = listItem.propertyCity!!.replace("\n"," ")
        val date: String = GlobalMethods.changeDateFormat(
            listItem.propertyCreatedDate,
            "yyyy-MM-dd",
            "dd MMM yyyy"
        )
        holder.binding.tvDate.text = date
        holder.binding.clMain.setOnClickListener {
            homeNavigator.onListedItemClick(listItem)
        }
    }

    override fun getItemCount(): Int {
        return propertyList.count()
    }
}