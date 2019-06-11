package com.mlsdev.mlsdevstore.data.model.order

import com.google.gson.annotations.SerializedName

data class PostOrderResult(
        @SerializedName("purchaseOrderHref")
        val purchaseOrderHref: String,
        @SerializedName("purchaseOrderId")
        val purchaseOrderId: String,
        @SerializedName("purchaseOrderPaymentStatus")
        val purchaseOrderPaymentStatus: OrderPaymentStatus)

enum class OrderPaymentStatus {
    @SerializedName("PENDING")
    PENDING,
    @SerializedName("PAID")
    PAID,
    @SerializedName("PARTIALLY_PAID")
    PARTIALLY_PAID,
    @SerializedName("FAILED")
    FAILED
}