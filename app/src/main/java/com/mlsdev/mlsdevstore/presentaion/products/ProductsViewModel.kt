package com.mlsdev.mlsdevstore.presentaion.products

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.mlsdev.mlsdevstore.data.DataLoadState
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.data.repository.ProductsRepository
import com.mlsdev.mlsdevstore.data.repository.SearchImageRepository
import com.mlsdev.mlsdevstore.presentaion.utils.ExtrasKeys
import com.mlsdev.mlsdevstore.presentaion.utils.Utils
import com.mlsdev.mlsdevstore.presentaion.viewmodel.BaseViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ProductsViewModel @Inject constructor(
        private val appUtils: Utils,
        private val productsRepository: ProductsRepository,
        private val searchImageRepository: SearchImageRepository
) : BaseViewModel() {

    lateinit var products: Observable<PagedList<Product>>
    val loadingState: LiveData<DataLoadState> by lazy { productsRepository.getPageLoadingState() }
    val categoryName = ObservableField<String>("")
    val categoryImage = ObservableField<String>()

    fun initCategoryData(categoryData: Bundle?) {
        checkNetworkConnection(appUtils) {
            val categoryId = categoryData?.getString(ExtrasKeys.KEY_CATEGORY_ID)
            categoryName.set(categoryData?.getString(ExtrasKeys.KEY_CATEGORY_NAME, "") ?: "")

            if (categoryId != null) {
                loadImageForCategory(categoryId, categoryName.get())
                products = productsRepository.getItems(categoryId)
            } else {
                throw Exception("'Category id' hasn't been passed")
            }
        }
    }

    private fun loadImageForCategory(categoryId: String?, categoryName: String?) {
        if (categoryId != null && categoryName != null && categoryName.isNotBlank()) {
            checkNetworkConnection(appUtils) {
                compositeDisposable.add(searchImageRepository.searchImage(categoryId, categoryName).subscribe(
                        { categoryImage.set(it.imageUrl) },
                        { /* Loading image error. It doesn't need be handled */ }
                ))
            }
        }
    }

    fun refresh() {
        checkNetworkConnection(appUtils) { productsRepository.refresh() }
    }

    fun retry() {
        checkNetworkConnection(appUtils) { productsRepository.retry() }
    }

}