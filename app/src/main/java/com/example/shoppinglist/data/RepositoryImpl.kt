package com.example.shoppinglist.data

import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository

object RepositoryImpl : ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()
    private var autoincrementId = 0

    init {
        for (i in 0..9) {
            shopList.add(ShopItem("Name$i", i, true))
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        // Если ID уже был установлен (когда попали сюда из редактирования существующего айтема)
        // то ID не меняем
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoincrementId++
        }

        shopList.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldItem = getShopItem(shopItem.id)
        shopList.remove(oldItem)
        addShopItem(shopItem)
    }

    override fun getShopItem(itemId: Int): ShopItem {
        return shopList.find { it.id == itemId }
            ?: throw RuntimeException("Item with id $itemId not found")
    }

    override fun getShopList(): List<ShopItem> {
        return shopList.toList() // возвращаем копию
    }

}