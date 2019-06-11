package com.mlsdev.mlsdevstore.data.remote.datasource

import androidx.paging.DataSource
import com.mlsdev.mlsdevstore.data.handleLoading
import com.mlsdev.mlsdevstore.data.local.Key
import com.mlsdev.mlsdevstore.data.local.LocalDataSource
import com.mlsdev.mlsdevstore.data.local.SharedPreferencesManager
import com.mlsdev.mlsdevstore.data.local.database.AppDatabase
import com.mlsdev.mlsdevstore.data.model.category.CategoryTreeNode
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.data.model.product.SearchResult
import com.mlsdev.mlsdevstore.data.remote.service.BrowseService
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Provider

// Data source
open class RandomProductsDataSource @Inject constructor(
        private val database: AppDatabase,
        private val browseService: BrowseService,
        private val sharedPreferencesManager: SharedPreferencesManager,
        private val localDataSource: LocalDataSource
) : BasePositionalDataSource<Product>() {

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Product>) {
        try {
            if ((params.startPosition + params.loadSize) >= totalCount) {
                callback.onResult(emptyList())
                return
            }

            val categoryId = sharedPreferencesManager[Key.RANDOM_CATEGORY_TREE_NODE, CategoryTreeNode::class.java]
                    ?.category?.categoryId ?: "0"

            disposable = browseService.searchItemsByCategoryId(categoryId, params.loadSize, params.startPosition)
                    .handleLoading(loadStateLiveData)
                    .subscribe(
                            { callback.onResult(it.itemSummaries) },
                            { handleError(it, params, callback) })

        } catch (e: Exception) {
            handleError(e, params, callback)
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Product>) {
        try {

            disposable = getRandomCategoryId()
                    .flatMap { categoryId -> searchProductsByCategoryId(categoryId, params) }
                    .map {
                        database.productsDao().deleteAllProducts()
                        database.productsDao().insert(it.itemSummaries)
                        return@map it
                    }
                    .subscribe(
                            { searchResult ->
                                totalCount = searchResult.total
                                callback.onResult(searchResult.itemSummaries, searchResult.offset, searchResult.total)
                            },
                            { handleError(it, params, callback) })
        } catch (e: Exception) {
            handleError(e, params, callback)
        }
    }

    private fun getRandomCategoryId(): Single<String> {
        return localDataSource.loadDefaultCategoryTreeId()
                .flatMap { localDataSource.loadRootCategoryTree() }
                .flatMap { Single.just(listOf(sharedPreferencesManager[Key.RANDOM_CATEGORY_TREE_NODE, CategoryTreeNode::class.java])) }
                .flatMap { categoryTreeNodes ->
                    return@flatMap if (categoryTreeNodes[0] == null) {
                        database.categoriesDao()
                                .queryCategoryTreeNode()
                                .subscribeOn(Schedulers.io())
                    } else Single.just(categoryTreeNodes)
                }
                .toFlowable()
                .flatMap { Flowable.fromIterable(it) }
                .filter { node -> node.category != null && node.category.categoryId != null }
                .toList()
                .map { nodes ->
                    val randomCategoryTreeNode: CategoryTreeNode = nodes[(Math.random() * nodes.size).toInt()]!!
                    sharedPreferencesManager.save(Key.RANDOM_CATEGORY_TREE_NODE, randomCategoryTreeNode)
                    return@map randomCategoryTreeNode.category.categoryId
                }
                .subscribeOn(Schedulers.io())
    }

    private fun searchProductsByCategoryId(categoryId: String, params: LoadInitialParams): Single<SearchResult> {
        return database.productsDao().queryAllProducts()
                .flatMap { products ->
                    if (products.isEmpty()) browseService.searchItemsByCategoryId(categoryId, params.pageSize, 0)
                    else {
                        val searchResult = SearchResult()
                        searchResult.itemSummaries = products
                        searchResult.total = if (products.size < 10) products.size else 100
                        searchResult.offset = 0
                        return@flatMap Single.just(searchResult)
                    }
                }
                .subscribeOn(Schedulers.io())
                .handleLoading(loadStateLiveData)
    }

}

// Data source factory
open class RandomProductsDataSourceFactory @Inject constructor(
        val provider: Provider<RandomProductsDataSource>
) : BasePositionDataSourceFactory<Int, Product>() {
    override fun create(): DataSource<Int, Product> {
        val dataSource = provider.get()
        dataSourceLiveData.postValue(dataSource)
        return dataSource
    }
}