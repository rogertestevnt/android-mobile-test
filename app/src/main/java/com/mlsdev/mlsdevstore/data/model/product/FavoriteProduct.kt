package com.mlsdev.mlsdevstore.data.model.product

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mlsdev.mlsdevstore.data.local.database.tables.FavoritesTable

@Entity(tableName = FavoritesTable.NAME)
data class FavoriteProduct(
        @Embedded
        val product: Product) {

    @PrimaryKey
    @ColumnInfo(name = FavoritesTable.COLUMN_ID)
    var id = product.itemId
}