package com.mlsdev.mlsdevstore.data.model.error

import com.google.gson.annotations.SerializedName

data class ErrorsWrapper(
        @SerializedName("errors")
        var errors: List<Error>?
)
