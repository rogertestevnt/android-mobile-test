package com.mlsdev.mlsdevstore.data.remote.service

import com.mlsdev.mlsdevstore.data.model.order.GuestCheckoutSession
import com.mlsdev.mlsdevstore.data.model.order.GuestCheckoutSessionRequest
import com.mlsdev.mlsdevstore.data.model.order.PostOrderResult
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * @see [eBay Order API documentation](https://developer.ebay.com/api-docs/buy/order/overview.html)
 * Use this api to retrieve categories and subcategories
 */
interface OrderService {

    @POST("buy/order/v1/guest_checkout_session/initiate")
    fun initiateGuestCheckoutSession(@Body body: GuestCheckoutSessionRequest): Single<GuestCheckoutSession>

    @POST("buy/order/v1/guest_checkout_session/{checkoutSessionId}/place_order")
    fun postOrder(@Path("checkoutSessionId") checkoutSessionId: String): Single<PostOrderResult>

}