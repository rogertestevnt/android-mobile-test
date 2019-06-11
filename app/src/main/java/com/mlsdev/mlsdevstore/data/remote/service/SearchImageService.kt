package com.mlsdev.mlsdevstore.data.remote.service

import com.mlsdev.mlsdevstore.BuildConfig
import com.mlsdev.mlsdevstore.data.model.image.SearchImagesPage
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchImageService {

    @GET("?key=${BuildConfig.SEARCH_API_KEY}&image_type=photo&per_page=3")
    fun searchImage(@Query("q") title: String): Single<SearchImagesPage>

}