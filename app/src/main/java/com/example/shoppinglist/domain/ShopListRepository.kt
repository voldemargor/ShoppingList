package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)

    fun getShopItem(itemId: Int) : ShopItem

    fun deleteShopItem(shopItem: ShopItem)

    fun editShopItem (newShopItem: ShopItem)

    fun getShopList() : LiveData<List<ShopItem>>

}