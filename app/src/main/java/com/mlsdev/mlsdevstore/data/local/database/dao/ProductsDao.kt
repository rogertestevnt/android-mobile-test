package com.mlsdev.mlsdevstore.data.local.database.dao

import androidx.room.*
import com.mlsdev.mlsdevstore.data.local.database.tables.ProductsTable
import com.mlsdev.mlsdevstore.data.model.product.Product
import io.reactivex.Single

@Dao
interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(products: List<Product>)

    @Query("select * from ${ProductsTable.NAME} limit 10")
    fun queryAllProducts(): Single<List<Product>>

    @Query("select * from ${ProductsTable.NAME}")
    fun queryAllProductsSync(): List<Product>

    @Delete
    fun delete(vararg product: Product)

    @Query("delete from ${ProductsTable.NAME}")
    fun deleteAllProducts()
}