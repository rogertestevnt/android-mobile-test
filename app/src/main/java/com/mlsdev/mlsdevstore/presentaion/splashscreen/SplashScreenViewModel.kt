package com.mlsdev.mlsdevstore.presentaion.splashscreen

import androidx.lifecycle.MutableLiveData
import com.mlsdev.mlsdevstore.data.local.Key
import com.mlsdev.mlsdevstore.data.local.SharedPreferencesManager
import com.mlsdev.mlsdevstore.data.model.authentication.AppAccessToken
import com.mlsdev.mlsdevstore.data.remote.datasource.RemoteDataSource
import com.mlsdev.mlsdevstore.presentaion.viewmodel.BaseViewModel
import java.util.*
import javax.inject.Inject

class SplashScreenViewModel @Inject
constructor(private val source: RemoteDataSource, private val preferencesManager: SharedPreferencesManager) : BaseViewModel() {
    val appAccessTokenLiveData = MutableLiveData<Boolean>()

    fun checkAuthentication() {
        preferencesManager.remove(Key.RANDOM_CATEGORY_TREE_NODE)
        val token = preferencesManager[Key.APPLICATION_ACCESS_TOKEN, AppAccessToken::class.java]
        val currentTime = Calendar.getInstance().timeInMillis
        if (token == null || token.expirationDate - currentTime <= 0) {
            compositeDisposable.add(source.appAccessToken.subscribe(
                    { accessToken: AppAccessToken? -> appAccessTokenLiveData.postValue(accessToken != null) },
                    { handleError(it) }))
        } else {
            appAccessTokenLiveData.postValue(true)
        }
    }
}