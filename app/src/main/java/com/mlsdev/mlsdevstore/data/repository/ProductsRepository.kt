package com.mlsdev.mlsdevstore.data.repository

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.mlsdev.mlsdevstore.data.DataLoadState
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.data.remote.datasource.BasePositionDataSourceFactory
import com.mlsdev.mlsdevstore.data.remote.datasource.getPagingConfig
import com.mlsdev.mlsdevstore.presentaion.utils.ExtrasKeys
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ProductsRepository @Inject constructor(
        private val dataSourceFactory: BasePositionDataSourceFactory<Int, Product>
) : BaseRepository() {

    open fun getItems(categoryId: String): Observable<PagedList<Product>> {
        dataSourceFactory.applyArguments(Bundle().apply { putString(ExtrasKeys.KEY_CATEGORY_ID, categoryId) })
        return RxPagedListBuilder(dataSourceFactory, getPagingConfig()).buildObservable()
    }

    open fun refresh() {
        dataSourceFactory.invalidateDataSource()
    }

    open fun retry() {
        dataSourceFactory.retry()
    }

    open fun getPageLoadingState(): LiveData<DataLoadState> =
            Transformations.switchMap(dataSourceFactory.getDataSourceLiveData()) { it.getDataLoadState() }

}