package com.mlsdev.mlsdevstore.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mlsdev.mlsdevstore.data.local.database.tables.SellersTable
import com.mlsdev.mlsdevstore.data.model.seller.Seller
import io.reactivex.Single

@Dao
interface SellersDao {

    @Insert
    fun insert(vararg seller: Seller)

    @Query("select * from ${SellersTable.NAME}")
    fun queryAllSellers(): Single<List<Seller>>

    @Delete
    fun delete(vararg seller: Seller)

}