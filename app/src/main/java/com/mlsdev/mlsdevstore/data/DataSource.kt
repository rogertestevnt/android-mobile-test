package com.mlsdev.mlsdevstore.data

import androidx.lifecycle.LiveData
import com.mlsdev.mlsdevstore.data.model.category.CategoryTree
import com.mlsdev.mlsdevstore.data.model.category.CategoryTreeNode
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.data.model.product.SearchResult
import io.reactivex.Single

interface DataSource {

    /**
     * Default category tree id is needed to get the root [CategoryTree] which contains
     * a list of [CategoryTreeNode]s.
     */
    fun loadDefaultCategoryTreeId(): Single<String>

    /**
     * The root [CategoryTree] contains all main [CategoryTreeNode]s.
     */
    fun loadRootCategoryTree(): Single<CategoryTree>

    /**
     * Deletes all data in the local data storage
     */
    fun refreshRootCategoryTree(): Single<CategoryTree>

    /**
     * @param queries - contains more than a category id.
     * @see [search documentation](https://developer.ebay.com/api-docs/buy/browse/resources/item_summary/methods/search.h2-input)
     */
    fun searchItemsByCategoryId(queries: Map<String, String>): Single<SearchResult>

    fun searchMoreItemsByRandomCategory(): Single<SearchResult>

    fun getItem(itemId: String): Single<Product>

    fun resetSearchResults()

    /**
     * @return the [LiveData] of the favorite [Product] list
     * */
    fun getFavoriteProducts(): LiveData<List<Product>>

    /**
     * Adds the [Product] into the favorites
     * @param product the [Product]
     * */
    suspend fun addToFavorites(product: Product)

    /**
     * Removes the [Product] from the favorites in the database
     * @param product the [Product]
     * */
    suspend fun removeFromFavorites(product: Product)

    /**
     * Checks if the favorite is in the favorite list
     * @param productId the checked favorite's id
     * */
    suspend fun isProductFavored(productId: String): Boolean
}
