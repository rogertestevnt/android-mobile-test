package com.mlsdev.mlsdevstore.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mlsdev.mlsdevstore.data.local.database.Table
import com.mlsdev.mlsdevstore.data.model.order.GuestCheckoutSession
import io.reactivex.Single

@Dao
interface CheckoutSessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg guestCheckoutSession: GuestCheckoutSession)

    @Query("select * from ${Table.GUEST_CHECKOUT_SESSION} where checkoutSessionId is :guestCheckoutSessionId limit 1")
    fun queryGuestCheckoutSession(guestCheckoutSessionId: String): Single<List<GuestCheckoutSession>>

}