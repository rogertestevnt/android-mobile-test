package com.mlsdev.mlsdevstore.data.local

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.mlsdev.mlsdevstore.data.cart.Cart
import com.mlsdev.mlsdevstore.data.model.product.CartTotalSum
import com.mlsdev.mlsdevstore.data.model.product.ListItem
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

class CartProductsDataSource @Inject constructor(
        val cart: Cart
) : PageKeyedDataSource<Int, ListItem>() {
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, ListItem>) {
        try {
            val cartItems = ArrayList<ListItem>(cart.items)
            if (cartItems.isNotEmpty())
                cartItems.add(getTotalSumFooterItem())
            callback.onResult(cartItems, null, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ListItem>) {
        callback.onResult(emptyList(), null)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ListItem>) {
        callback.onResult(emptyList(), null)
    }

    private fun getTotalSumFooterItem(): ListItem = CartTotalSum(cart.getTotalSum())

}

@Singleton
class CartItemsDataSourceFactory @Inject constructor(
        private val dataSourceProvider: Provider<CartProductsDataSource>
) : DataSource.Factory<Int, ListItem>() {

    val currentDataSource = MutableLiveData<DataSource<Int, ListItem>>()

    override fun create(): DataSource<Int, ListItem> {
        val dataSource = dataSourceProvider.get()
        currentDataSource.postValue(dataSource)
        return dataSource
    }

}

