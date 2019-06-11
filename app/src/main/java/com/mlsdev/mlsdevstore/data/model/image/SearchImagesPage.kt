package com.mlsdev.mlsdevstore.data.model.image

import com.google.gson.annotations.SerializedName

data class SearchImagesPage(
        @SerializedName("hits")
        val images: List<SearchedImage>
) {
    fun getImageUrl(): String? = images.getOrNull(0)?.imageUrl
}