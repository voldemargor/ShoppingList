package com.example.shoppinglist.domain

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun editShopItem (shopItem: ShopItem)

    fun getShopItem(itemId: Int) : ShopItem

    fun getShopList() : List<ShopItem>

}