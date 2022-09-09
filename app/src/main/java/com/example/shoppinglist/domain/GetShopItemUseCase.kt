package com.example.shoppinglist.domain

class GetShopItemUseCase(
    private val repository: ShopListRepository
) {

    fun getShopItem(itemId : Int) : ShopItem {
        return repository.getShopItem(itemId)
    }

}