package com.mlsdev.mlsdevstore.presentaion.favorites

import androidx.lifecycle.LiveData
import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.data.DataSource
import com.mlsdev.mlsdevstore.data.model.message.AlertMessage
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.presentaion.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

open class FavoritesViewModel @Inject constructor(
        private val dataSource: DataSource
) : BaseViewModel() {

    val favoriteProducts: LiveData<List<Product>> = dataSource.getFavoriteProducts()

    fun addProductIntoFavorites(product: Product) {
        viewModelScope.launch {
            try {
                dataSource.addToFavorites(product)
            } catch (e: Exception) {
                val message = AlertMessage.Builder()
                        .title(R.string.error_title_base)
                        .message(R.string.error_add_to_favorites)
                        .build()
                messageLiveData.postValue(message)
            }
        }
    }

    fun removeProductFromFavorites(product: Product) {
        viewModelScope.launch {
            try {
                dataSource.removeFromFavorites(product)
            } catch (e: Exception) {
                val message = AlertMessage.Builder()
                        .title(R.string.error_title_base)
                        .message(R.string.error_remove_from_favorites)
                        .build()
                messageLiveData.postValue(message)
            }
        }
    }

}