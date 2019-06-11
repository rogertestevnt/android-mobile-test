package com.mlsdev.mlsdevstore.data.remote.service

import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.data.model.product.SearchResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface BrowseService {

    /**
     * @see [eBay Browse API :: Search documentation](https://developer.ebay.com/api-docs/buy/browse/resources/item_summary/methods/search)
     * Use this api to search products
     */
    @GET("/buy/browse/v1/item_summary/search")
    fun searchItemsByCategoryId(@QueryMap queries: Map<String, String>): Single<SearchResult>

    /**
     * @see [eBay Browse API :: Search documentation](https://developer.ebay.com/api-docs/buy/browse/resources/item_summary/methods/search)
     * Use this api to search products
     */
    @GET("/buy/browse/v1/item_summary/search")
    fun searchItemsByCategoryId(
            @Query("category_ids") categoryIds: String,
            @Query("limit") limit: Int,
            @Query("offset") offset: Int
    ): Single<SearchResult>

    @GET("/buy/browse/v1/item/{id}?fieldgroups=PRODUCT")
    fun getItem(@Path("id") itemId: String): Single<Product>

}
