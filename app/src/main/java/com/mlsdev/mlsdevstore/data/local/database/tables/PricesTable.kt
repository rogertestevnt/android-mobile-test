package com.mlsdev.mlsdevstore.data.local.database.tables

abstract class PricesTable {
    companion object {
        const val NAME = "products"
        const val COLUMN_ID = "price_id"
        const val COLUMN_PRICE = "price"
        const val COLUMN_CURRENCY = "currency"
    }
}