package com.rentalhomes.ui.buyer.createtemplate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rentalhomes.R
import com.rentalhomes.data.network.model.responseModel.GetTier3
import com.rentalhomes.databinding.RowTemplateItemBinding

class TemplateItemAdapter(
    private val context: Context,
    private var templateItemList: List<GetTier3.Category>,
    private var navigator: CreateTemplateNavigator
) :
    RecyclerView.Adapter<TemplateItemAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: RowTemplateItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowTemplateItemBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowTemplateItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_template_item,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvTemplateItem.text = templateItemList[position].featureName

        //Hide last position view divider
        if (position == templateItemList.size - 1) {
            holder.binding.viewDivider.visibility = View.GONE
        }

        //Set visibility of star on click
        if (templateItemList[position].visible == 1) {
            holder.binding.ivStar.setImageResource(R.drawable.ic_yellow_star)
        } else {
            holder.binding.ivStar.setImageResource(R.drawable.ic_grey_star)
        }

        //Click of star
        holder.binding.ivStar.setOnClickListener {
            if (templateItemList[position].visible == 1)
                templateItemList[position].visible = 0
            else if (templateItemList[position].visible == 0)
                templateItemList[position].visible = 1
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return templateItemList.count()
    }
}