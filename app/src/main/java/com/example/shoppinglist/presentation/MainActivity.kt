package com.example.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopListLiveData.observe(this) {
            adapter.submitList(it)
        }

        button_add_shop_item.setOnClickListener() {

            if (isOnePaneMode())
                startActivity(ShopItemActivity.newIntentAddItem(this))
            else
                launchFragment(ShopItemFragment.newInstanceAddItem())
        }
    }

    override fun onEditingFinish() {
        Toast.makeText(this@MainActivity, "Saved", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    private fun isOnePaneMode(): Boolean {
        val shopItemContainer = shop_item_fragment_container
        return shopItemContainer == null
    }

    private fun launchFragment(fragment: ShopItemFragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(shop_item_fragment_container.id, fragment)
            // почему addToBackStack(null) и как передать имя с 21 минуты
            // https://stepik.org/lesson/709305/step/1?unit=709868
            .addToBackStack(null)
            .commit()
    }


    private fun setupRecyclerView() {
        adapter = ShopListAdapter()
        rv_shop_list.adapter = adapter
        rv_shop_list.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.VIEW_TYPE_ENABLED,
            ShopListAdapter.MAX_POOL_SIZE
        )
        rv_shop_list.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.VIEW_TYPE_DISABLED,
            ShopListAdapter.MAX_POOL_SIZE
        )

        setupClickListener()
        setupLongClickListener()
        setupSwipeListener(rv_shop_list)

    }

    private fun setupClickListener() {
        adapter.onShopItemClickListener = {
            Log.d("mylog", it.toString())

            if (isOnePaneMode())
                startActivity(ShopItemActivity.newIntentEditItem(this, it.id))
            else
                launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
        }
    }

    private fun setupLongClickListener() {
        adapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {

        val callback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

}
