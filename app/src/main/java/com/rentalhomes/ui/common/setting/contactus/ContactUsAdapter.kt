package com.rentalhomes.ui.common.setting.contactus

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rentalhomes.R
import com.rentalhomes.databinding.RowContactUsBinding

class ContactUsAdapter(
    private val context: Context,
    private var contactUsList: ArrayList<ContactUsModel>,
    private var contactUsNavigator: ContactUsNavigator
) :
    RecyclerView.Adapter<ContactUsAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: RowContactUsBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding: RowContactUsBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowContactUsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_contact_us,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (position == contactUsList.count() - 1) {
            holder.binding.vdContactItem.visibility = View.GONE
        }
        holder.binding.ivIcon.setImageResource(contactUsList[position].icon)
        holder.binding.tvContactTitle.text = contactUsList[position].contactTitle
        holder.binding.tvContactEmail.text = contactUsList[position].contactEmail

        holder.binding.clContactUsItems.setOnClickListener {
            contactUsNavigator.onSettingItemClick(position, contactUsList[position].contactEmail)
        }

    }

    override fun getItemCount(): Int {
        return contactUsList.count()
    }
}
