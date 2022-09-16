package com.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var llShopList: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        llShopList = findViewById(R.id.ll_shop_list)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.shopListLiveData.observe(this) {
            updateList(it)
        }
    }

    private fun updateList(list: List<ShopItem>) {

        llShopList.removeAllViews()

        list.forEach {

            val layoutId =
                if (it.enabled) R.layout.shop_item_enabled
                else R.layout.shop_item_disabled

            val view = LayoutInflater.from(this).inflate(layoutId, llShopList, false)
            val tvName = view.findViewById<TextView>(R.id.tv_shop_item_text)
            val tvCount = view.findViewById<TextView>(R.id.tv_item_count)
            tvName.text = it.name
            tvCount.text = it.count.toString()

            val shopItem = it
            view.setOnLongClickListener {
                viewModel.changeEnableState(shopItem)
                true
            }

            llShopList.addView(view)
        }

    }

}