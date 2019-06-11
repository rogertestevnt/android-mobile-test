package com.mlsdev.mlsdevstore.data.local.database.tables

abstract class ProductsTable {
    companion object {
        const val NAME = "products_table"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_PRICE = "price"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_IS_FAVORITE = "is_favorite"
    }
}