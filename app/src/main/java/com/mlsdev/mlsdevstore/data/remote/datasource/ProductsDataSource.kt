package com.mlsdev.mlsdevstore.data.remote.datasource

import android.os.Bundle
import androidx.paging.DataSource
import com.mlsdev.mlsdevstore.data.DataLoadState
import com.mlsdev.mlsdevstore.data.handleLoading
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.data.remote.service.BrowseService
import com.mlsdev.mlsdevstore.presentaion.utils.ExtrasKeys
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Data source
 * */
open class ProductsDataSource @Inject constructor(
        private val browseService: BrowseService
) : BasePositionalDataSource<Product>() {
    var categoryId: String = "0"

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Product>) {

        if ((params.startPosition + params.loadSize) >= totalCount) {
            callback.onResult(emptyList())
            return
        }

        try {
            disposable = browseService.searchItemsByCategoryId(categoryId, params.loadSize, params.loadSize + params.startPosition)
                    .handleLoading(loadStateLiveData)
                    .subscribe(
                            { callback.onResult(it.itemSummaries) },
                            { handleError(it, params, callback) })
        } catch (exception: Exception) {
            handleError(exception, params, callback)
        }

    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Product>) {

        try {
            loadStateLiveData.postValue(DataLoadState.LOADING)
            disposable = browseService.searchItemsByCategoryId(categoryId, params.pageSize, 0)
                    .handleLoading(loadStateLiveData)
                    .subscribe(
                            {
                                totalCount = it.total
                                callback.onResult(it.itemSummaries, it.offset, it.total)
                            },
                            { handleError(it, params, callback) })
        } catch (exception: Exception) {
            handleError(exception, params, callback)
        }

    }

}

/**
 * Data source factory
 * */
@Singleton
open class ProductsDataSourceFactory @Inject constructor(
        val provider: Provider<ProductsDataSource>
) : BasePositionDataSourceFactory<Int, Product>() {

    var categoryId: String = "0"

    override fun create(): DataSource<Int, Product> {
        val productsDataSource = provider.get()
        productsDataSource.categoryId = categoryId
        dataSourceLiveData.postValue(productsDataSource)
        return productsDataSource
    }

    override fun applyArguments(bundle: Bundle) {
        bundle.getString(ExtrasKeys.KEY_CATEGORY_ID)?.let {
            categoryId = it
        }
    }
}