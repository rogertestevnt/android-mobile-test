package com.mlsdev.mlsdevstore.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.mlsdev.mlsdevstore.data.local.database.Table
import com.mlsdev.mlsdevstore.data.model.user.PersonalInfo

import io.reactivex.Single

@Dao
interface PersonalInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(personalInfo: PersonalInfo)

    @Update
    fun update(personalInfo: PersonalInfo)

    @Delete
    fun delete(personalInfo: PersonalInfo)

    @Query("select * from ${Table.PERSONAL_INFO} limit 1")
    fun queryPersonalInfo(): Single<List<PersonalInfo>>

    @Query("select * from ${Table.PERSONAL_INFO} limit 1")
    fun queryPersonalInfoSync(): List<PersonalInfo>

}
