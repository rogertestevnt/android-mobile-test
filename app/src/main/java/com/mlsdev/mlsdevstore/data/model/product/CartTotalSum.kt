package com.mlsdev.mlsdevstore.data.model.product

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartTotalSum(
        val value: Double
) : ListItem, Parcelable {
    override fun getId(): String = "total_sum"

    override fun getItemTitle(): String = ""

    override fun getImageUrl(): String = ""

    override fun getItemPrice(): Price {
        val price = Price()
        price.value = value
        return price
    }

    override fun getItemCondition(): String = ""

    override fun getParcelable(): Parcelable = this
}