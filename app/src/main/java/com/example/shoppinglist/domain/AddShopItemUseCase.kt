package com.example.shoppinglist.domain

class AddShopItemUseCase(
    private val repository: ShopListRepository
) {

    fun addShopItem(shopItem: ShopItem) {
        repository.addShopItem(shopItem)
    }

}