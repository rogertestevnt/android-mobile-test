package com.mlsdev.mlsdevstore.presentaion.utils

import java.util.regex.Pattern

class CreditCardTypeDetector {

    enum class Type(val value: String) {
        UNKNOWN("Unknown") {
            override fun pattern(): Pattern? = null
        },
        VISA("Visa") {
            override fun pattern(): Pattern? = Pattern.compile("4[0-9]{12}(?:[0-9]{3})?")
        },
        MASTERCARD("MasterCard") {
            override fun pattern(): Pattern? = Pattern.compile("5[1-5][0-9]{14}")
        },
        AMERICAN_EXPRESS("AmEx") {
            override fun pattern(): Pattern? = Pattern.compile("3[47][0-9]{13}")
        },
        DISCOVER("Discover") {
            override fun pattern(): Pattern? = Pattern.compile("6(?:011|5[0-9]{2})[0-9]{12}")
        };

        abstract fun pattern(): Pattern?
    }

    fun getCardType(cardNumber: String): Type {
        val onlyDigitsCardNumber = cardNumber
                .replace(" ", "")
                .replace("-", "")

        for (type in Type.values()) {
            if (type.pattern() == null)
                continue

            if (type.pattern()?.matcher(onlyDigitsCardNumber)?.matches() == true)
                return type
        }
        return Type.UNKNOWN
    }

    fun isValid(cardNumber: String?): Boolean {
        if (cardNumber.isNullOrBlank())
            return false

        val onlyDigitsCardNumber = cardNumber
                .replace(" ", "")
                .replace("-", "")

        var sum = 0
        var alternate = false
        for (i in onlyDigitsCardNumber.length - 1 downTo 0) {
            var n = Integer.parseInt(onlyDigitsCardNumber.substring(i, i + 1))
            if (alternate) {
                n *= 2
                if (n > 9) {
                    n = n % 10 + 1
                }
            }
            sum += n
            alternate = !alternate
        }
        return sum % 10 == 0
    }
}
