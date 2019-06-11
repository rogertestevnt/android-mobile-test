package com.mlsdev.mlsdevstore.stub.data.local

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.mlsdev.mlsdevstore.data.model.product.CartTotalSum
import com.mlsdev.mlsdevstore.data.model.product.ListItem
import com.mlsdev.mlsdevstore.data.model.product.Product

class CartProductsDataSourceStub(
        private val products: List<Product>,
        private val totalSum: Double
) : PageKeyedDataSource<Int, ListItem>() {
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, ListItem>) {
        try {
            val cartItems = ArrayList<ListItem>(products)
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

    private fun getTotalSumFooterItem(): ListItem = CartTotalSum(totalSum)

}

class CartItemsDataSourceFactoryStub(
        private val products: List<Product>,
        private val totalSum: Double
) : DataSource.Factory<Int, ListItem>() {

    override fun create(): DataSource<Int, ListItem> {
        val dataSource = CartProductsDataSourceStub(products, totalSum)
        return dataSource
    }

}

