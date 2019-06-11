package com.mlsdev.mlsdevstore.data.model.user

import com.google.gson.annotations.SerializedName

const val AMEX = "AmEx"
const val VISA = "Visa"
const val MASTERCARD = "MasterCard"
const val DISCOVER = "Discover"

data class CreditCard(
        @SerializedName("brand")
        var brand: String? = null,
        @SerializedName("cardNumber")
        var cardNumber: String? = null,
        @SerializedName("cvvNumber")
        var cvvNumber: String? = null,
        @SerializedName("expireMonth")
        var expireMonth: Int = 0,
        @SerializedName("expireYear")
        var expireYear: Int = 0,
        @SerializedName("accountHolderName")
        var accountHolderName: String?,
        @SerializedName("billingAddress")
        var billingAddress: BillingAddress)
