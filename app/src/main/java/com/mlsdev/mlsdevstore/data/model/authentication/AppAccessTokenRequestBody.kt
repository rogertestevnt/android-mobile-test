package com.mlsdev.mlsdevstore.data.model.authentication

import com.google.gson.annotations.SerializedName
import com.mlsdev.mlsdevstore.BuildConfig
import java.util.*

class AppAccessTokenRequestBody {

    @SerializedName("grant_type")
    private val grantType = "client_credentials"

    @SerializedName("redirect_uri")
    private val redirectUri: String = BuildConfig.REDIRECT_URI

    @SerializedName("scope")
    private val scope = "https://api.ebay.com/oauth/api_scope " +
            "https://api.ebay.com/oauth/api_scope/buy.guest.order"

    val fields: Map<String, String>
        get() {
            val fields = HashMap<String, String>(3)

            fields["grant_type"] = grantType
            fields["redirect_uri"] = redirectUri
            fields["scope"] = scope

            return fields
        }

}
