package com.mlsdev.mlsdevstore.presentaion.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

fun isInputCorrect(s: Editable?, size: Int, dividerPosition: Int, divider: Char): Boolean {
    if (s.isNullOrBlank())
        return false

    var isCorrect = s.length <= size
    for (i in 0 until s.length) {
        isCorrect = if (i > 0 && (i + 1) % dividerPosition == 0) isCorrect and (divider == s[i])
        else isCorrect and Character.isDigit(s[i])
    }
    return isCorrect
}

fun concatString(digits: CharArray, dividerPosition: Int, divider: Char): String {
    val formatted = StringBuilder()

    for (i in digits.indices) {
        if (digits[i].toInt() != 0) {
            formatted.append(digits[i])
            if (i > 0 && i < digits.size - 1 && (i + 1) % dividerPosition == 0) {
                formatted.append(divider)
            }
        }
    }

    return formatted.toString()
}

fun getDigitArray(s: CharSequence, size: Int): CharArray {
    val digits = CharArray(size)
    var index = 0
    var i = 0
    while (i < s.length && index < size) {
        val current = s[i]
        if (Character.isDigit(current)) {
            digits[index] = current
            index++
        }
        i++
    }
    return digits
}

class CreditCardEditText @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    var onTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s != null && !isInputCorrect(s, CARD_NUMBER_TOTAL_SYMBOLS, CARD_NUMBER_DIVIDER_MODULO, CARD_NUMBER_DIVIDER)) {
                s.replace(0, s.length, concatString(getDigitArray(s, CARD_NUMBER_TOTAL_DIGITS), CARD_NUMBER_DIVIDER_POSITION, CARD_NUMBER_DIVIDER));
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }

    init {
        addTextChangedListener(onTextChangeListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeTextChangedListener(onTextChangeListener)
    }

    companion object {
        const val CARD_NUMBER_TOTAL_SYMBOLS = 19 // size of pattern 0000-0000-0000-0000
        const val CARD_NUMBER_DIVIDER = ' '
        const val CARD_NUMBER_DIVIDER_MODULO = 5 // means divider position is every 5th symbol beginning with 1
        const val CARD_NUMBER_DIVIDER_POSITION = CARD_NUMBER_DIVIDER_MODULO - 1 // means divider position is every 4th symbol beginning with 0
        const val CARD_NUMBER_TOTAL_DIGITS = 16 // max numbers of digits in pattern: 0000 x 4
    }

}

class CardExpirationEditText @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    var onTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s != null && !isInputCorrect(s, CARD_DATE_TOTAL_SYMBOLS, CARD_DATE_DIVIDER_MODULO, CARD_DATE_DIVIDER)) {
                s.replace(0, s.length, concatString(getDigitArray(s, CARD_DATE_TOTAL_DIGITS), CARD_DATE_DIVIDER_POSITION, CARD_DATE_DIVIDER));
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }

    init {
        addTextChangedListener(onTextChangeListener)
    }

    companion object {
        const val CARD_DATE_TOTAL_SYMBOLS = 5 // size of pattern MM/YY
        const val CARD_DATE_TOTAL_DIGITS = 4 // max numbers of digits in pattern: MM + YY
        const val CARD_DATE_DIVIDER_MODULO = 3 // means divider position is every 3rd symbol beginning with 1
        const val CARD_DATE_DIVIDER_POSITION = CARD_DATE_DIVIDER_MODULO - 1 // means divider position is every 2nd symbol beginning with 0
        const val CARD_DATE_DIVIDER = '/'
    }

}