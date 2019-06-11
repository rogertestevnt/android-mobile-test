package com.mlsdev.mlsdevstore.presentaion.store

import androidx.databinding.ObservableField
import com.mlsdev.mlsdevstore.data.model.product.Condition
import com.mlsdev.mlsdevstore.data.model.product.ListItem
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.presentaion.utils.CustomObservableBoolean

class ProductItemViewModel(
        private val removeFromCartListener: ((productId: String) -> Unit)?
) {
    private var listItem: ListItem? = null
    val title = ObservableField<String>()
    val priceFirstPart = ObservableField<String>()
    val priceSecondPart = ObservableField<String>()
    val currency = ObservableField<String>()
    val imageUrl = ObservableField<String>()
    val isNew = CustomObservableBoolean()
    val isFavorite = CustomObservableBoolean()

    fun setItem(listItem: ListItem) {
        this.listItem = listItem

        val priceArray = listItem.itemPrice.value.toString().split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        title.set(listItem.itemTitle)
        if (priceArray.isNotEmpty()) {
            priceFirstPart.set(priceArray[0])
            if (priceArray.size == 1)
                priceArray[1] = "00"

            if (priceArray[1].length == 1)
                priceArray[1] = priceArray[1] + "0"

            priceSecondPart.set(priceArray[1])
            currency.set(listItem.itemPrice.currency)
        }
        imageUrl.set(listItem.imageUrl)
        isNew.set(listItem.itemCondition != null && listItem.itemCondition == Condition.New)
        isFavorite.set((listItem as Product).isFavorite)
    }

    fun removeFromCart() {
        removeFromCartListener?.let { it(listItem!!.id) }
    }

}
