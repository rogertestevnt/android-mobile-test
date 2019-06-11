package com.mlsdev.mlsdevstore.data.model.product

import android.os.Parcelable
import androidx.room.*
import com.mlsdev.mlsdevstore.data.local.database.tables.ProductsTable
import com.mlsdev.mlsdevstore.data.model.seller.Seller
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = ProductsTable.NAME)
data class Product(
        @PrimaryKey
        @ColumnInfo(name = ProductsTable.COLUMN_ID)
        var itemId: String = "id",
        @ColumnInfo(name = ProductsTable.COLUMN_TITLE)
        var title: String?,
        @Embedded var image: Image?,
        @Embedded var price: Price?,
        @Ignore var seller: Seller?,
        @Ignore var itemHref: String?,
        @Condition var condition: String?,
        @Ignore var description: String?,
        @Ignore var brand: String?,
        @Ignore var size: String?,
        @Ignore var gender: String?,
        @Ignore var color: String?,
        @Ignore var material: String?,
        @Ignore var adultOnly: Boolean,
        @Ignore var additionalImages: ArrayList<Image> = arrayListOf(),
        @ColumnInfo(name = ProductsTable.COLUMN_IS_FAVORITE)
        var isFavorite: Boolean = false
) : Parcelable, ListItem {
    override fun getId(): String = itemId

    override fun getItemTitle(): String = title ?: ""

    override fun getImageUrl(): String = image?.imageUrl ?: ""

    override fun getItemPrice(): Price = price ?: Price(0.0)

    override fun getItemCondition(): String = condition ?: Condition.New

    override fun getParcelable(): Parcelable = this

    constructor() : this("id", null, null, null, null, null,
            null, null, null, null, null, null,
            null, false)
}

annotation class Condition {
    companion object {
        val New = "New"
        val Used = "Used"
    }
}