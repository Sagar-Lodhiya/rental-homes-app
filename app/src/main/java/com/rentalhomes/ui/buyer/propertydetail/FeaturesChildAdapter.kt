package com.rentalhomes.ui.buyer.propertydetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rentalhomes.R
import com.rentalhomes.data.network.model.responseModel.GetTier3
import com.rentalhomes.databinding.RowFeaturesChildBinding

class FeaturesChildAdapter(
    private val context: Context,
    private var featuresChildList: List<GetTier3.Category>,
    private var navigator: BuyerPropertyDetailNavigator
) :
    RecyclerView.Adapter<FeaturesChildAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: RowFeaturesChildBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowFeaturesChildBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowFeaturesChildBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_features_child,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvFeaturesItem.text = featuresChildList[position].featureName

        if (position == featuresChildList.size - 1) {
            holder.binding.viewDivider.visibility = View.GONE
        }

        when (featuresChildList[position].like) {
            1 -> {
                holder.binding.ivLike.setImageResource(R.drawable.ic_like)
                holder.binding.ivDisLike.setImageResource(R.drawable.ic_dislike_grey)
            }
            2 -> {
                holder.binding.ivDisLike.setImageResource(R.drawable.ic_dislike)
                holder.binding.ivLike.setImageResource(R.drawable.ic_like_grey)
            }
            else -> {
                holder.binding.ivLike.setImageResource(R.drawable.ic_like_grey)
                holder.binding.ivDisLike.setImageResource(R.drawable.ic_dislike_grey)
            }
        }

        holder.binding.ivLike.setOnClickListener {
            if (featuresChildList[position].like == 1) {
                featuresChildList[position].like = 0
            } else
                featuresChildList[position].like = 1
            navigator.updateProgressBar()
            notifyItemChanged(position)
        }

        holder.binding.ivDisLike.setOnClickListener {
            if (featuresChildList[position].like == 2) {
                featuresChildList[position].like = 0
            } else
                featuresChildList[position].like = 2
            navigator.updateProgressBar()
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return featuresChildList.count()
    }
}