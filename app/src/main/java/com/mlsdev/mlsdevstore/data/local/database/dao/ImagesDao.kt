package com.mlsdev.mlsdevstore.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mlsdev.mlsdevstore.data.local.database.tables.ImagesTable
import com.mlsdev.mlsdevstore.data.model.product.Image
import io.reactivex.Single

@Dao
interface ImagesDao {

    @Insert
    fun insert(vararg image: Image)

    @Query("select * from ${ImagesTable.NAME}")
    fun queryAllImages(): Single<List<Image>>

    @Delete
    fun delete(vararg image: Image)

}