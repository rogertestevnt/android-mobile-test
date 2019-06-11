package com.mlsdev.mlsdevstore.data.local.database.dao

import androidx.room.*

import com.mlsdev.mlsdevstore.data.local.database.Table
import com.mlsdev.mlsdevstore.data.model.user.Address

import io.reactivex.Single

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg addresses: Address)

    @Delete
    fun delete(vararg addresses: Address)

    @Update
    fun update(vararg addresses: Address)

    @Query("select * from ${Table.ADDRESSES} where type is :addressType limit 1")
    fun queryByType(@Address.Type addressType: String): Single<List<Address>>

    @Query("select * from ${Table.ADDRESSES} where type is :addressType limit 1")
    fun queryByTypeSync(@Address.Type addressType: String): List<Address>

}
