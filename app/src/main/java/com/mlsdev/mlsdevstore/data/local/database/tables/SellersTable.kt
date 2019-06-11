package com.mlsdev.mlsdevstore.data.local.database.tables

abstract class SellersTable {
    companion object {
        const val NAME = "sellers"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_FEEDBACK_SCORE = "feedback_score"
        const val COLUMN_FEEDBACK_PERCENTAGE = "feedback_percentage"
    }
}