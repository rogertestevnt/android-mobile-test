package com.mlsdev.mlsdevstore.presentaion.utils

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import androidx.collection.ArrayMap
import com.mlsdev.mlsdevstore.R
import io.reactivex.Completable
import java.util.*
import javax.inject.Inject

const val EMAIL = "email"
const val FIRST_NAME = "first_name"
const val LAST_NAME = "last_name"
const val FIELD_ADDRESS = "field_address"
const val FIELD_CITY = "field_city"
const val FIELD_PHONE = "field_phone"
const val FIELD_STATE = "field_state"
const val FIELD_POSTAL_CODE = "field_postal_code"

open class FieldsValidator @Inject
constructor(private val context: Context) {
    private val fieldsForValidation: MutableMap<String, String?>
    private val invalidFields: MutableMap<String, String>

    init {
        fieldsForValidation = HashMap()
        invalidFields = ArrayMap()
    }

    open fun putField(key: String, value: String?): FieldsValidator {
        fieldsForValidation[key] = value
        return this
    }

    open fun validateFields(): Completable {
        return Completable.create { emitter ->
            invalidFields.clear()

            for ((key, value) in fieldsForValidation)
                if (TextUtils.isEmpty(value))
                    invalidFields[key] = context.getString(R.string.error_empty_field)

            if (!invalidFields.containsKey(EMAIL) && fieldsForValidation.containsKey(EMAIL) && !validateEmail(fieldsForValidation[EMAIL]))
                invalidFields[EMAIL] = context.getString(R.string.error_incorrect_field)

            fieldsForValidation.clear()

            if (invalidFields.isEmpty())
                emitter.onComplete()
            else
                emitter.onError(ValidationError(invalidFields))
        }
    }

    private fun validateEmail(email: String?): Boolean {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    class ValidationError(private val invalidFields: Map<String, String>) : Throwable() {
        fun getErrorForField(field: String): String? = invalidFields[field]
    }
}
