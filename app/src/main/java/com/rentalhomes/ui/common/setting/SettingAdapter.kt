package com.rentalhomes.ui.common.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rentalhomes.R
import com.rentalhomes.databinding.RowSettingItemsBinding

class SettingAdapter(
    private val context: Context,
    private var settingList: ArrayList<SettingModel>,
    private var settingNavigator: SettingNavigator
) :
    RecyclerView.Adapter<SettingAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: RowSettingItemsBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding: RowSettingItemsBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowSettingItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_setting_items,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == settingList.count() - 1) {
            holder.binding.vdSettingItem.visibility = View.GONE
        }
        holder.binding.ivItemIcon.setImageResource(settingList[position].img)
        holder.binding.tvItemName.text = settingList[position].name

        holder.binding.clSettingItems.setOnClickListener {
            settingNavigator.onSettingItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return settingList.count()
    }
}