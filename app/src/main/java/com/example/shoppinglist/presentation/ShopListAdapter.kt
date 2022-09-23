package com.example.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    companion object {
        var counterViewHolder = 0

        const val VIEW_TYPE_ENABLED = 100
        const val VIEW_TYPE_DISABLED = 200

        const val MAX_POOL_SIZE = 15
    }

    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_shop_item_text)
        val tvCount = view.findViewById<TextView>(R.id.tv_item_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {

        Log.d("mylog", "onCreateViewHolder: ${++counterViewHolder}")

        val layoutId = when(viewType) {
            VIEW_TYPE_ENABLED -> R.layout.shop_item_enabled
            VIEW_TYPE_DISABLED -> R.layout.shop_item_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }

        val view =
            LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val item = shopList[position]
        holder.tvName.text = item.name
        holder.tvCount.text = item.count.toString()
        holder.view.setOnLongClickListener {
            true
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = shopList[position]
        return if (item.enabled) VIEW_TYPE_ENABLED else VIEW_TYPE_DISABLED
    }

    override fun getItemCount(): Int {
        return shopList.size
    }
}