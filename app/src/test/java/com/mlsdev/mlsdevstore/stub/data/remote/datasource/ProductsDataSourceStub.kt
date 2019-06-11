package com.mlsdev.mlsdevstore.stub.data.remote.datasource

import androidx.paging.DataSource
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.data.remote.datasource.BasePositionDataSourceFactory
import com.mlsdev.mlsdevstore.data.remote.datasource.BasePositionalDataSource

// Data source
class RandomProductsDataSourceStub : BasePositionalDataSource<Product>() {

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Product>) {
        try {
            val productStub = Product()
            productStub.itemId = "product_id"
            callback.onResult(listOf(productStub))
        } catch (e: Exception) {
            handleError(e, params, callback)
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Product>) {
        try {
            callback.onResult(listOf(Product()), 0, 1)
        } catch (e: Exception) {
            handleError(e, params, callback)
        }
    }

}

// Data source factory
class ProductsDataSourceFactoryStub : BasePositionDataSourceFactory<Int, Product>() {
    override fun retry() {

    }

    override fun create(): DataSource<Int, Product> {
        val dataSource = RandomProductsDataSourceStub()
        dataSourceLiveData.postValue(dataSource)
        return dataSource
    }
}