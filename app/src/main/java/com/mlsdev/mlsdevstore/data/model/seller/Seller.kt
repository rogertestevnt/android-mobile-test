package com.mlsdev.mlsdevstore.data.model.seller

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mlsdev.mlsdevstore.data.local.database.tables.SellersTable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = SellersTable.NAME)
data class Seller(
        @ColumnInfo(name = SellersTable.COLUMN_FEEDBACK_SCORE)
        val feedbackScore: Double,
        @ColumnInfo(name = SellersTable.COLUMN_NAME)
        val username: String?,
        @ColumnInfo(name = SellersTable.COLUMN_FEEDBACK_PERCENTAGE)
        val feedbackPercentage: String?
) : Parcelable {

    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SellersTable.COLUMN_ID)
    var id: Long = 0

    fun getFeedbackRating(): String {
        return feedbackPercentage ?: "0"
    }

}
