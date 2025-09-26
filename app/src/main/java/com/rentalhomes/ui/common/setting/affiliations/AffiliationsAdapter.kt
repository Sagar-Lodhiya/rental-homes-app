package com.rentalhomes.ui.common.setting.affiliations

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rentalhomes.R
import com.rentalhomes.data.network.model.responseModel.GetAffiliationsResponse
import com.rentalhomes.databinding.RowAffiliationsBinding

class AffiliationsAdapter(
    private val context: Context,
    private var affiliationsList: ArrayList<GetAffiliationsResponse.Affiliations>,
    private var affiliationsNavigator: AffiliationsNavigator
) :
    RecyclerView.Adapter<AffiliationsAdapter.MyViewHolder>() {

    fun updateList(affiliationsList: ArrayList<GetAffiliationsResponse.Affiliations>) {
        this.affiliationsList = affiliationsList
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: RowAffiliationsBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding: RowAffiliationsBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowAffiliationsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_affiliations,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(affiliationsList[position].image).into(holder.binding.ivAffiliations)


        holder.binding.clAffiliationsItems.setOnClickListener {
            affiliationsNavigator.run {
                onAffiliationsItemClick(
                    position,
                    affiliationsList[position].link.toString()
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return affiliationsList.count()
    }
}