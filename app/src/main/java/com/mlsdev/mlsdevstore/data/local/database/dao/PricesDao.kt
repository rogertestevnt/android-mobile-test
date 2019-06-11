package com.mlsdev.mlsdevstore.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mlsdev.mlsdevstore.data.local.database.tables.PricesTable
import com.mlsdev.mlsdevstore.data.model.product.Price
import io.reactivex.Single

@Dao
interface PricesDao {

    @Insert
    fun insert(vararg price: Price)

    @Query("select * from ${PricesTable.NAME}")
    fun queryAllPrices(): Single<List<Price>>

    @Delete
    fun delete(vararg price: Price)

}