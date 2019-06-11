package com.mlsdev.mlsdevstore.data.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey

import com.mlsdev.mlsdevstore.data.local.database.Table

@Entity(tableName = Table.PERSONAL_INFO)
class PersonalInfo {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var contactEmail: String? = null
    var contactFirstName: String? = null
    var contactLastName: String? = null
}
