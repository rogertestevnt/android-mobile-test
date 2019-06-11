package com.mlsdev.mlsdevstore.data.model.product

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.mlsdev.mlsdevstore.data.local.database.tables.PricesTable
import kotlinx.android.parcel.Parcelize

@Parcelize
//@Entity(tableName = PricesTable.NAME)
data class Price(
        @ColumnInfo(name = PricesTable.COLUMN_PRICE)
        var value: Double = 0.0,
        @ColumnInfo(name = PricesTable.COLUMN_CURRENCY)
        var currency: String? = null
) : Parcelable {

//    @IgnoredOnParcel
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = PricesTable.COLUMN_ID)
//    var id: Long = 0

}
