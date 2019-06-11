package com.mlsdev.mlsdevstore.data.validator

import android.app.Application
import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.data.model.error.ValidationException
import com.mlsdev.mlsdevstore.data.model.order.GuestCheckoutSessionRequest
import io.reactivex.Single

class GuestCheckoutSessionValidator(val app: Application) : Validator<GuestCheckoutSessionRequest> {
    override fun validate(data: GuestCheckoutSessionRequest): Single<GuestCheckoutSessionRequest> {
        val invalidFields = HashMap<String, String>()

        if (data.contactEmail.isNullOrBlank()
                || data.contactFirstName.isNullOrBlank()
                || data.contactLastName.isNullOrBlank()
                || data.creditCard.brand.isNullOrBlank()
                || data.creditCard.accountHolderName.isNullOrBlank()
                || data.creditCard.billingAddress.firstName.isBlank()
                || data.creditCard.billingAddress.lastName.isBlank()
                || data.shippingAddress.recipient.isNullOrBlank()
                || data.shippingAddress.address.isNullOrBlank()
                || data.shippingAddress.city.isNullOrBlank()
                || data.shippingAddress.phoneNumber.isNullOrBlank()
                || data.shippingAddress.postalCode.isNullOrBlank()) {
            invalidFields[FIELD_INIT_CHECKOUT_REQUEST] = app.getString(R.string.error_incorrect_personal_info)
        }

        return if (invalidFields.isEmpty()) Single.just(data)
        else Single.error(ValidationException(invalidFields))
    }
}