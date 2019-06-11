package com.mlsdev.mlsdevstore.data.model.order

import com.google.gson.annotations.SerializedName

data class LineItemInput(
        @SerializedName("itemId")
        val itemId: String,
        @SerializedName("quantity")
        val quantity: Int)