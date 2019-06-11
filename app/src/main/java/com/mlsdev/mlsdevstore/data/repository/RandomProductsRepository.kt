package com.mlsdev.mlsdevstore.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.mlsdev.mlsdevstore.data.DataLoadState
import com.mlsdev.mlsdevstore.data.local.Key
import com.mlsdev.mlsdevstore.data.local.SharedPreferencesManager
import com.mlsdev.mlsdevstore.data.local.database.AppDatabase
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.data.remote.datasource.BasePositionDataSourceFactory
import com.mlsdev.mlsdevstore.data.remote.datasource.getPagingConfig
import com.mlsdev.mlsdevstore.injections.Named
import com.mlsdev.mlsdevstore.injections.module.DataSourceFactoryType
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class RandomProductsRepository @Inject constructor(
        private val database: AppDatabase,
        private val prefsManager: SharedPreferencesManager,
        @Named(DataSourceFactoryType.RANDOM)
        private val dataSourceFactory: BasePositionDataSourceFactory<Int, Product>
) : BaseRepository() {

    open fun getItems(): Observable<PagedList<Product>> =
            RxPagedListBuilder(dataSourceFactory, getPagingConfig()).buildObservable()

    open fun refresh() {
        database.deleteAllProducts().subscribe()
        prefsManager.remove(Key.RANDOM_CATEGORY_TREE_NODE)
        dataSourceFactory.invalidateDataSource()
    }

    open fun retry() {
        dataSourceFactory.retry()
    }

    open fun getPageLoadingState(): LiveData<DataLoadState> =
            Transformations.switchMap(dataSourceFactory.getDataSourceLiveData()) { it.getDataLoadState() }

}