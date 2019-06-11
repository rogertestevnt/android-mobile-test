package com.mlsdev.mlsdevstore.data.model.order

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.mlsdev.mlsdevstore.data.local.database.Table

@Entity(tableName = Table.GUEST_CHECKOUT_SESSION)
data class GuestCheckoutSession(
        @PrimaryKey(autoGenerate = false)
        @SerializedName("checkoutSessionId")
        val checkoutSessionId: String
)