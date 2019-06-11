package com.mlsdev.mlsdevstore.data.remote.service

import com.mlsdev.mlsdevstore.data.model.category.CategoryTree

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @see [eBay Taxonomy API documentation](https://developer.ebay.com/api-docs/commerce/taxonomy/overview.html)
 * Use this api to retrieve categories and subcategories
 */
interface TaxonomyService {

    @get:GET("commerce/taxonomy/v1_beta/get_default_category_tree_id?marketplace_id=EBAY_US")
    val defaultCategoryTreeId: Single<CategoryTree>

    @GET("commerce/taxonomy/v1_beta/category_tree/{category_tree_id}")
    fun getCategoryTree(@Path("category_tree_id") categoryTreeId: String): Single<CategoryTree>

    @GET("commerce/taxonomy/v1_beta/category_tree/{category_tree_id}/get_category_subtree")
    fun getCategorySubtree(@Path("category_tree_id") categoryTreeId: String,
                           @Query("category_id") categoryId: String): Single<CategoryTree>

}
