package com.mlsdev.mlsdevstore.presentaion.store

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.mlsdev.mlsdevstore.data.DataLoadState
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.data.repository.RandomProductsRepository
import com.mlsdev.mlsdevstore.presentaion.utils.Utils
import com.mlsdev.mlsdevstore.presentaion.viewmodel.BaseViewModel
import io.reactivex.Observable
import javax.inject.Inject

open class StoreViewModel @Inject
constructor(
        private val appUtils: Utils,
        private val repository: RandomProductsRepository
) : BaseViewModel() {

    val products: Observable<PagedList<Product>> by lazy { repository.getItems() }
    val loadingState: LiveData<DataLoadState> by lazy { repository.getPageLoadingState() }

    open fun refresh() {
        checkNetworkConnection(appUtils) { repository.refresh() }
    }

    open fun retry() {
        checkNetworkConnection(appUtils) { repository.retry() }
    }
}
