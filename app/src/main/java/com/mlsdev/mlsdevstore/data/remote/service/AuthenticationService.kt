package com.mlsdev.mlsdevstore.data.remote.service

import com.mlsdev.mlsdevstore.data.model.authentication.AppAccessToken

import io.reactivex.Single
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface AuthenticationService {

    @FormUrlEncoded
    @POST("identity/v1/oauth2/token")
    fun getAppAccessToken(@HeaderMap headers: Map<String, String>,
                          @FieldMap fieldMap: Map<String, String>): Single<AppAccessToken>

}
