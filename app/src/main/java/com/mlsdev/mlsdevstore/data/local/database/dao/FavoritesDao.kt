package com.mlsdev.mlsdevstore.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mlsdev.mlsdevstore.data.local.database.tables.FavoritesTable
import com.mlsdev.mlsdevstore.data.model.product.FavoriteProduct

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg favorite: FavoriteProduct)

    @Delete
    fun delete(vararg favorite: FavoriteProduct)

    @Query("delete from ${FavoritesTable.NAME}")
    fun deleteAllFavorites()

    @Query("delete from ${FavoritesTable.NAME} where ${FavoritesTable.COLUMN_ID} = :id")
    fun delete(id: String)

    @Query("select * from ${FavoritesTable.NAME}")
    fun getFavorites(): LiveData<List<FavoriteProduct>>

    @Query("select count(${FavoritesTable.COLUMN_ID}) from ${FavoritesTable.NAME} where ${FavoritesTable.COLUMN_ID} = :productId")
    fun checkIfExists(productId: String): Int
}