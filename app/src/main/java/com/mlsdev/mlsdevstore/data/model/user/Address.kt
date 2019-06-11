package com.mlsdev.mlsdevstore.data.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey

import com.google.gson.annotations.SerializedName
import com.mlsdev.mlsdevstore.data.local.database.Table

@Entity(tableName = Table.ADDRESSES)
class Address {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @SerializedName("addressLine1")
    var address: String? = null
    @SerializedName("city")
    var city: String? = null
    @SerializedName("country")
    var country: String? = null
    @SerializedName("county")
    var county: String? = null
    @SerializedName("phoneNumber")
    var phoneNumber: String? = null
    @SerializedName("postalCode")
    var postalCode: String? = null
    @SerializedName("recipient")
    var recipient: String? = null
    @SerializedName("stateOrProvince")
    var state: String? = null
    @Type
    @get:Type
    var type = Type.SHIPPING

    annotation class Type {
        companion object {
            const val SHIPPING = "shipping"
            const val BILLING = "billing"
        }
    }
}
