package com.example.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter :
    ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    companion object {
        var counterOnCreateViewHolder = 0
        var counterOnBindViewHolder = 0

        const val VIEW_TYPE_ENABLED = 100
        const val VIEW_TYPE_DISABLED = 200

        const val MAX_POOL_SIZE = 15
    }

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {

//        Log.d("mylog", "onCreateViewHolder: ${++counterOnCreateViewHolder}")

        val layoutId = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.shop_item_enabled
            VIEW_TYPE_DISABLED -> R.layout.shop_item_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }

        val view =
            LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {

        Log.d("mylog", "onBindViewHolder: ${++counterOnBindViewHolder}")

        val item = getItem(position)
        holder.tvName.text = item.name
        holder.tvCount.text = item.count.toString()

        holder.view.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(item)
            true
        }

        holder.view.setOnClickListener {
            onShopItemClickListener?.invoke(item)
        }

    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.enabled) VIEW_TYPE_ENABLED else VIEW_TYPE_DISABLED
    }

}