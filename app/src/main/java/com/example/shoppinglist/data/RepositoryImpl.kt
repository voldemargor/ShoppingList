package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import kotlin.random.Random

object RepositoryImpl : ShopListRepository {

    private val shopListLiveData = MutableLiveData<List<ShopItem>>()
    private val shopList = sortedSetOf<ShopItem>({ p0, p1 -> p0.id.compareTo(p1.id) })
    private var autoincrementId = 0

    init {
        for (i in 0..100) {
            addShopItem(ShopItem("Name$i", i, Random.nextBoolean()))
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        // Если ID уже был установлен (когда попали сюда из редактирования существующего айтема)
        // то ID не меняем
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoincrementId++
        }

        shopList.add(shopItem)

        updateListLiveData()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateListLiveData()
    }

    override fun editShopItem(newShopItem: ShopItem) {
        val oldItem = getShopItem(newShopItem.id)
        shopList.remove(oldItem)
        addShopItem(newShopItem)
    }

    override fun getShopItem(itemId: Int): ShopItem {
        return shopList.find { it.id == itemId }
            ?: throw RuntimeException("Item with id $itemId not found")
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLiveData
    }

    private fun updateListLiveData() {
        shopListLiveData.value = shopList.toList() // устанавливаем копию
    }

}