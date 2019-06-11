package com.mlsdev.mlsdevstore.data.model.message

import androidx.annotation.StringRes
import com.mlsdev.mlsdevstore.R

data class AlertMessage(
        @StringRes val title: Int,
        @StringRes val message: Int,
        @StringRes val positiveButton: Int?,
        @StringRes val negativeButton: Int?,
        val kind: Kind
) {
    enum class Kind {
        INFO,
        ERROR
    }

    class Builder {
        @StringRes
        private var title: Int? = null
        @StringRes
        private var message: Int? = null
        @StringRes
        private var positiveButton: Int? = null
        @StringRes
        private var negativeButton: Int? = null
        private var kind: Kind = Kind.INFO

        fun title(@StringRes title: Int): Builder =
                this.also { this.title = title }

        fun message(@StringRes message: Int): Builder =
                this.also { this.message = message }

        fun positiveButton(@StringRes positiveButton: Int): Builder =
                this.also { this.positiveButton = positiveButton }

        fun negativeButton(@StringRes negativeButton: Int): Builder =
                this.also { this.negativeButton = negativeButton }

        fun kind(kind: Kind): Builder =
                this.also { this.kind = kind }

        fun build(): AlertMessage = AlertMessage(
                title ?: R.string.error_title_base,
                message ?: R.string.error_message_common,
                positiveButton,
                negativeButton,
                kind)
    }
}