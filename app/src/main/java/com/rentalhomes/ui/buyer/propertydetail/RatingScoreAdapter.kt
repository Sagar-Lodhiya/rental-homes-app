package com.rentalhomes.ui.buyer.propertydetail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rentalhomes.R
import com.rentalhomes.databinding.RowRatingScoreBinding
import com.rentalhomes.utils.customviews.RegularTextView

class RatingScoreAdapter(
    private val context: Context,
    private var ratingScoreList: ArrayList<RatingScoreModel>,
    private var navigator: BuyerPropertyDetailNavigator
) :
    RecyclerView.Adapter<RatingScoreAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: RowRatingScoreBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowRatingScoreBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowRatingScoreBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_rating_score,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvRatingName.text = ratingScoreList[position].ratingTitle

        holder.binding.cbNA.isChecked = ratingScoreList[position].isNA

        /**
         * Rating Score
         * @param 0 = Zero
         * @param 1 = Low
         * @param 3 = Meets
         * @param 5 = Higher
         * @param 6 = Exceeds Expectations
         */

        when (ratingScoreList[position].ratingScore) {
            0 -> {
                setAlpha(
                    holder.binding.tvZero,
                    holder.binding.tvLow,
                    holder.binding.tvMeets,
                    holder.binding.tvHigher,
                    holder.binding.tvExceeds
                )
            }
            1 -> {
                setAlpha(
                    holder.binding.tvLow,
                    holder.binding.tvZero,
                    holder.binding.tvMeets,
                    holder.binding.tvHigher,
                    holder.binding.tvExceeds
                )
            }
            3 -> {
                setAlpha(
                    holder.binding.tvMeets,
                    holder.binding.tvZero,
                    holder.binding.tvLow,
                    holder.binding.tvHigher,
                    holder.binding.tvExceeds
                )
            }
            5 -> {
                setAlpha(
                    holder.binding.tvHigher,
                    holder.binding.tvZero,
                    holder.binding.tvLow,
                    holder.binding.tvMeets,
                    holder.binding.tvExceeds
                )
            }
            6 -> {
                setAlpha(
                    holder.binding.tvExceeds,
                    holder.binding.tvZero,
                    holder.binding.tvLow,
                    holder.binding.tvMeets,
                    holder.binding.tvHigher
                )
            }
            10 -> {
                /*holder.binding.tvZero.isEnabled = false
                holder.binding.tvLow.isEnabled = false
                holder.binding.tvMeets.isEnabled = false
                holder.binding.tvHigher.isEnabled = false
                holder.binding.tvExceeds.isEnabled = false*/

                holder.binding.tvZero.alpha = 0.4F
                holder.binding.tvLow.alpha = 0.4F
                holder.binding.tvMeets.alpha = 0.4F
                holder.binding.tvHigher.alpha = 0.4F
                holder.binding.tvExceeds.alpha = 0.4F

                ratingScoreList[position].isNA = true
                holder.binding.cbNA.isChecked = true
            }
        }

        holder.binding.tvZero.setOnClickListener {
            ratingScoreList[position].ratingScore = 0

            if (holder.binding.cbNA.isChecked) {
                holder.binding.cbNA.isChecked = false
                ratingScoreList[position].isNA = false
            }

            setAlpha(
                holder.binding.tvZero,
                holder.binding.tvLow,
                holder.binding.tvMeets,
                holder.binding.tvHigher,
                holder.binding.tvExceeds
            )
        }
        holder.binding.tvLow.setOnClickListener {
            ratingScoreList[position].ratingScore = 1

            if (holder.binding.cbNA.isChecked) {
                holder.binding.cbNA.isChecked = false
                ratingScoreList[position].isNA = false
            }

            setAlpha(
                holder.binding.tvLow,
                holder.binding.tvZero,
                holder.binding.tvMeets,
                holder.binding.tvHigher,
                holder.binding.tvExceeds
            )
        }
        holder.binding.tvMeets.setOnClickListener {
            ratingScoreList[position].ratingScore = 3

            if (holder.binding.cbNA.isChecked) {
                holder.binding.cbNA.isChecked = false
                ratingScoreList[position].isNA = false
            }

            setAlpha(
                holder.binding.tvMeets,
                holder.binding.tvZero,
                holder.binding.tvLow,
                holder.binding.tvHigher,
                holder.binding.tvExceeds
            )
        }
        holder.binding.tvHigher.setOnClickListener {
            ratingScoreList[position].ratingScore = 5

            if (holder.binding.cbNA.isChecked) {
                holder.binding.cbNA.isChecked = false
                ratingScoreList[position].isNA = false
            }

            setAlpha(
                holder.binding.tvHigher,
                holder.binding.tvZero,
                holder.binding.tvLow,
                holder.binding.tvMeets,
                holder.binding.tvExceeds
            )
        }
        holder.binding.tvExceeds.setOnClickListener {
            ratingScoreList[position].ratingScore = 6

            if (holder.binding.cbNA.isChecked) {
                holder.binding.cbNA.isChecked = false
                ratingScoreList[position].isNA = false
            }

            setAlpha(
                holder.binding.tvExceeds,
                holder.binding.tvZero,
                holder.binding.tvLow,
                holder.binding.tvMeets,
                holder.binding.tvHigher
            )
        }

        holder.binding.cbNA.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                /*holder.binding.tvZero.isEnabled = false
                holder.binding.tvLow.isEnabled = false
                holder.binding.tvMeets.isEnabled = false
                holder.binding.tvHigher.isEnabled = false
                holder.binding.tvExceeds.isEnabled = false*/

                holder.binding.tvZero.alpha = 0.4F
                holder.binding.tvLow.alpha = 0.4F
                holder.binding.tvMeets.alpha = 0.4F
                holder.binding.tvHigher.alpha = 0.4F
                holder.binding.tvExceeds.alpha = 0.4F

                ratingScoreList[position].isNA = b
                ratingScoreList[position].ratingScore = 10
            } else {
                holder.binding.tvZero.isEnabled = true
                holder.binding.tvLow.isEnabled = true
                holder.binding.tvMeets.isEnabled = true
                holder.binding.tvHigher.isEnabled = true
                holder.binding.tvExceeds.isEnabled = true

                holder.binding.tvZero.alpha = 0.4F
                holder.binding.tvLow.alpha = 0.4F
                holder.binding.tvMeets.alpha = 0.4F
                holder.binding.tvHigher.alpha = 0.4F
                holder.binding.tvExceeds.alpha = 0.4F

                ratingScoreList[position].isNA = b
                ratingScoreList[position].ratingScore = 100
            }
        }
    }

    override fun getItemCount(): Int {
        return ratingScoreList.count()
    }

    private fun setAlpha(
        tv1: RegularTextView,
        tv2: RegularTextView,
        tv3: RegularTextView,
        tv4: RegularTextView,
        tv5: RegularTextView
    ) {
        tv1.alpha = 1F
        tv2.alpha = 0.4F
        tv3.alpha = 0.4F
        tv4.alpha = 0.4F
        tv5.alpha = 0.4F
    }
}