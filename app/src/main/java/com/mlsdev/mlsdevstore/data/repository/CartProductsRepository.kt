package com.mlsdev.mlsdevstore.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.mlsdev.mlsdevstore.data.cart.Cart
import com.mlsdev.mlsdevstore.data.local.CartItemsDataSourceFactory
import com.mlsdev.mlsdevstore.data.model.product.ListItem
import com.mlsdev.mlsdevstore.data.remote.datasource.getPagingConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class CartProductsRepository @Inject constructor(
        val cart: Cart,
        val dataSourceFactory: CartItemsDataSourceFactory
) {

    init {
        cart.addOnItemCountChangeListener(object : Cart.OnItemCountChangeListener {
            override fun onItemCountChanged(count: Int) {
                dataSourceFactory.currentDataSource.value?.invalidate()
            }
        })
    }

    open fun getProducts(): LiveData<PagedList<ListItem>> =
            LivePagedListBuilder(dataSourceFactory, getPagingConfig()).build()

}