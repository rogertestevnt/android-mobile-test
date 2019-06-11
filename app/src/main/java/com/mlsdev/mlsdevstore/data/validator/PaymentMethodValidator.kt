package com.mlsdev.mlsdevstore.data.validator

import android.app.Application
import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.data.model.error.ValidationException
import com.mlsdev.mlsdevstore.presentaion.utils.CreditCardTypeDetector
import io.reactivex.Single
import java.util.*

class PaymentMethodValidator(val app: Application) : Validator<PaymentMethod> {

    override fun validate(data: PaymentMethod): Single<PaymentMethod> {
        val invalidFields = HashMap<String, String>()

        if (!validateExpirationDate(data.cardExpiration)) {
            invalidFields[FIELD_EXPIRATION_DATE] = app.getString(R.string.error_incorrect)
        }

        if (data.cardHolderName.isNullOrBlank()) {
            invalidFields[FIELD_CARD_HOLDER] = app.getString(R.string.error_empty)
        }

        if (data.cardNumber.isNullOrBlank()) {
            invalidFields[FIELD_CARD_NUMBER] = app.getString(R.string.error_empty)
        } else if (!CreditCardTypeDetector().isValid(data.cardNumber)) {
            invalidFields[FIELD_CARD_NUMBER] = app.getString(R.string.error_incorrect)
        }

        if (data.cvv.isNullOrBlank()) {
            invalidFields[FIELD_CVV] = app.getString(R.string.error_empty)
        }

        return if (invalidFields.isEmpty()) Single.just(data)
        else Single.error(ValidationException(invalidFields))
    }

    private fun validateExpirationDate(data: String?): Boolean {
        val yearsOffset = 10
        val maxMathValue = 12
        val maxYearValue = Calendar.getInstance().get(Calendar.YEAR) + yearsOffset
        val divider = '/'
        val monthAndYear: List<String>? = data?.split(divider)
        return (monthAndYear?.size == 2) &&
                (monthAndYear[0].toInt() <= maxMathValue) &&
                (monthAndYear[1].toInt() <= maxYearValue)
    }

}