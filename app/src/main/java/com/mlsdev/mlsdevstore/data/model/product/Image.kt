package com.mlsdev.mlsdevstore.data.model.product

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.mlsdev.mlsdevstore.data.local.database.tables.ImagesTable
import kotlinx.android.parcel.Parcelize

@Parcelize
//@Entity(tableName = ImagesTable.NAME)
data class Image(
        @ColumnInfo(name = ImagesTable.COLUMN_URL)
        val imageUrl: String?
) : Parcelable {

//    @IgnoredOnParcel
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = ImagesTable.COLUMN_ID)
//    var id: Long = 0

}
