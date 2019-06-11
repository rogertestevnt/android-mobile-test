package com.mlsdev.mlsdevstore.data.model.image

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mlsdev.mlsdevstore.data.local.database.Table

@Entity(tableName = Table.CATEGORY_IMAGES)
data class CategoryImageEntity(
        @PrimaryKey(autoGenerate = false)
        val categoryId: String,
        val imageUrl: String?
)