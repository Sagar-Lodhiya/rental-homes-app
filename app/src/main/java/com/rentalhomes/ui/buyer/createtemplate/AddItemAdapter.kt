package com.rentalhomes.ui.buyer.createtemplate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rentalhomes.R
import com.rentalhomes.databinding.RowCustomTemplateItemBinding

class AddItemAdapter(
    private val context: Context,
    private var itemList: ArrayList<String>,
    private var navigator: CreateTemplateNavigator
) :
    RecyclerView.Adapter<AddItemAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: RowCustomTemplateItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: RowCustomTemplateItemBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowCustomTemplateItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_custom_template_item,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //Set item name
//        holder.binding.tvTemplateItem.text = addItemList[position].itemName
        holder.binding.tvTemplateItem.text = itemList[position]

        holder.binding.ivDeleteItem.setOnClickListener {
            //Removed items from list and refresh list
//            addItemList.removeAt(holder.absoluteAdapterPosition)
            itemList.removeAt(holder.absoluteAdapterPosition)
            notifyItemRemoved(holder.absoluteAdapterPosition)
            //Show EditText and add item button after deleted items from list
            navigator.onAfterDeletedItem(position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }
}
