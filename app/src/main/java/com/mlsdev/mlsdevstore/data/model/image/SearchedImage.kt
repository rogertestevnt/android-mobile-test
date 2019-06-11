package com.mlsdev.mlsdevstore.data.model.image

import com.google.gson.annotations.SerializedName

data class SearchedImage(
        @SerializedName("webformatURL")
        val imageUrl: String
)