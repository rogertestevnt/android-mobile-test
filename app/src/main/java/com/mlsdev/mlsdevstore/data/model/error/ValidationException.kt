package com.mlsdev.mlsdevstore.data.model.error

class ValidationException(
        val invalidFields: Map<String, String>
) : Exception() {

    fun getErrorForField(field: String): String? = invalidFields[field]

}