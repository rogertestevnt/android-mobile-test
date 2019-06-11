package com.mlsdev.mlsdevstore.data.model.user

import com.google.gson.annotations.SerializedName

data class BillingAddress(
        @SerializedName("addressLine1")
        var address: String? = null,
        @SerializedName("city")
        var city: String? = null,
        @SerializedName("country")
        var country: String? = null,
        @SerializedName("county")
        var county: String? = null,
        @SerializedName("postalCode")
        var postalCode: String? = null,
        @SerializedName("stateOrProvince")
        var state: String? = null,
        @SerializedName("firstName")
        val firstName: String,
        @SerializedName("lastName")
        val lastName: String
) {
    constructor(address: Address, firstName: String?, lastName: String?) : this(
            address.address,
            address.city,
            address.country,
            address.county,
            address.postalCode,
            address.state,
            firstName ?: "",
            lastName ?: ""
    )
}
