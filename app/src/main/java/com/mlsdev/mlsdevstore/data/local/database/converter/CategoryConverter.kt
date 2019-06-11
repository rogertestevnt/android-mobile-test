package com.mlsdev.mlsdevstore.data.local.database.converter

import androidx.room.TypeConverter

import com.google.gson.Gson
import com.mlsdev.mlsdevstore.data.model.category.Category

object CategoryConverter {

    @TypeConverter
    fun toJson(category: Category?): String? {
        return if (category != null) Gson().toJson(category) else null
    }

    @TypeConverter
    fun toModel(json: String?): Category? {
        return if (json != null && !json.isEmpty()) Gson().fromJson(json, Category::class.java) else null
    }

}
