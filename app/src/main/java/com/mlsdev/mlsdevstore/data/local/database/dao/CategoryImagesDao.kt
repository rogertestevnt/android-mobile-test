package com.mlsdev.mlsdevstore.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mlsdev.mlsdevstore.data.local.database.Table
import com.mlsdev.mlsdevstore.data.model.image.CategoryImageEntity
import io.reactivex.Single

@Dao
interface CategoryImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg categoryImageEntity: CategoryImageEntity)

    @Query("select * from ${Table.CATEGORY_IMAGES} where categoryId is :categoryId limit 1")
    fun queryCategoryImageEntity(categoryId: String): Single<List<CategoryImageEntity>>

}