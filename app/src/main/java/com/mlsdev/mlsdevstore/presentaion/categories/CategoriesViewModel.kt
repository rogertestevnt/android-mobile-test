package com.mlsdev.mlsdevstore.presentaion.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mlsdev.mlsdevstore.data.DataSource
import com.mlsdev.mlsdevstore.data.model.category.CategoryTreeNode
import com.mlsdev.mlsdevstore.presentaion.utils.Utils
import com.mlsdev.mlsdevstore.presentaion.viewmodel.BaseViewModel
import javax.inject.Inject

open class CategoriesViewModel @Inject
constructor(
        private val dataSource: DataSource,
        private val utils: Utils
) : BaseViewModel() {

    private val categories = MutableLiveData<List<CategoryTreeNode>>()

    fun getCategories(): LiveData<List<CategoryTreeNode>> = categories.also { fetchRootCategories() }

    private fun fetchRootCategories() {
        checkNetworkConnection(utils) {
            setIsLoading(true)
            compositeDisposable.add(dataSource.loadRootCategoryTree().subscribe(
                    { categories.postValue(it.categoryTreeNode.childCategoryTreeNodes) },
                    { handleError(it) }))
        }
    }
}
