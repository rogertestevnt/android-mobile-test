package com.mlsdev.mlsdevstore.presentaion.utils

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.annotation.ColorInt
import java.util.*

class SpannableBuilder {
    private val spannables: MutableList<Spannable>

    private val lastSpannable: Spannable
        get() {
            if (spannables.isEmpty())
                throw IllegalArgumentException("Add at leas one element")

            return spannables[spannables.size - 1]
        }

    init {
        this.spannables = ArrayList()
    }

    fun append(text: String): SpannableBuilder {
        val spannable = SpannableString(text)
        spannables.add(spannable)
        return this
    }

    fun bold(): SpannableBuilder {
        lastSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, lastSpannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    fun underline(): SpannableBuilder {
        lastSpannable.setSpan(UnderlineSpan(), 0, lastSpannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    fun italic(): SpannableBuilder {
        lastSpannable.setSpan(StyleSpan(Typeface.ITALIC), 0, lastSpannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    fun color(@ColorInt color: Int): SpannableBuilder {
        lastSpannable.setSpan(ForegroundColorSpan(color), 0, lastSpannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    fun build(): Spannable {
        val builder = SpannableStringBuilder()

        for (i in spannables.indices) {
            builder.append(spannables[i])
            if (i + 1 < spannables.size && !spannables[i].toString().contains("\n"))
                builder.append(" ")
        }

        return builder
    }
}