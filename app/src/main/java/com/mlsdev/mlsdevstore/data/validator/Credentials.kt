package com.mlsdev.mlsdevstore.data.validator

data class PaymentMethod(
        val cardNumber: String?,
        val cardExpiration: String?,
        val cardHolderName: String?,
        val cvv: String?
)