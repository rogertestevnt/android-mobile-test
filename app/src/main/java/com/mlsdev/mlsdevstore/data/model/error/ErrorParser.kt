package com.mlsdev.mlsdevstore.data.model.error

import com.google.gson.Gson
import retrofit2.HttpException

class ErrorParser : Parser<Throwable, ErrorsWrapper> {
    override fun parse(input: Throwable): ErrorsWrapper = try {
        if (input is HttpException) {
            val errorResponseBodyJson = input.response().body().toString()
            Gson().fromJson(errorResponseBodyJson, ErrorsWrapper::class.java)
        } else {
            ErrorsWrapper(arrayListOf())
        }
    } catch (exp: Exception) {
        ErrorsWrapper(arrayListOf())
    }
}