package com.mlsdev.mlsdevstore.data.model.authentication

import com.google.gson.annotations.SerializedName

class AppAccessToken {
    @SerializedName("access_token")
    val accessToken: String? = null

    @SerializedName("expires_in")
    val expiresIn: Int? = null

    @SerializedName("refresh_token")
    val refreshToken: String? = null

    @SerializedName("token_type")
    val tokenType: String? = null

    @SerializedName("expiration_date")
    var expirationDate: Long = 0

    fun getExpiresIn(): Int {
        return expiresIn ?: 0 * 1000
    }
}