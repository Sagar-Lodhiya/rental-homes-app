package com.rentalhomes.ui.agent.buyerFeedback

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rentalhomes.R
import com.rentalhomes.databinding.RowFeedbackDataBinding

class BuyerFeedbackAdapter(
    private val context: Context,
    private val ratingScoreList: ArrayList<BuyerFeedbackModel>,
) :
    RecyclerView.Adapter<BuyerFeedbackAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: RowFeedbackDataBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowFeedbackDataBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowFeedbackDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_feedback_data,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = ratingScoreList[position]
        holder.binding.lblName.text = item.itemName
        when (item.ratingScore) {
            0 -> {
                holder.binding.tvFeedback.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    R.drawable.ic_zero
                )
                holder.binding.tvFeedback.text = context.getString(R.string.zero)
            }
            1 -> {
                holder.binding.tvFeedback.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    R.drawable.ic_low
                )
                holder.binding.tvFeedback.text = context.getString(R.string.low)
            }
            3 -> {
                holder.binding.tvFeedback.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    R.drawable.ic_meets
                )
                holder.binding.tvFeedback.text = context.getString(R.string.meets)
            }
            5 -> {
                holder.binding.tvFeedback.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    R.drawable.ic_higher
                )
                holder.binding.tvFeedback.text = context.getString(R.string.higher)
            }
            6 -> {
                holder.binding.tvFeedback.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    R.drawable.ic_exceeds
                )
                holder.binding.tvFeedback.text = context.getString(R.string.exceeds)
            }
            else -> {
                holder.binding.tvFeedback.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    0
                )
                holder.binding.tvFeedback.text = context.getString(R.string.n_a)
            }
        }
    }

    override fun getItemCount(): Int {
        return ratingScoreList.size
    }

}