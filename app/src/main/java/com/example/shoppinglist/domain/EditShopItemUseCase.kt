package com.example.shoppinglist.domain

class EditShopItemUseCase (
    private val repository: ShopListRepository
) {

    fun editShopItem(newShopItem: ShopItem) {
        repository.editShopItem(newShopItem)
    }

}

