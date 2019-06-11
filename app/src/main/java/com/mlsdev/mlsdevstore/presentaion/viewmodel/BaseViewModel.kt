package com.mlsdev.mlsdevstore.presentaion.viewmodel

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mlsdev.mlsdevstore.data.DataLoadState
import com.mlsdev.mlsdevstore.data.model.error.ErrorParser
import com.mlsdev.mlsdevstore.data.model.error.ValidationException
import com.mlsdev.mlsdevstore.data.model.message.AlertMessage
import com.mlsdev.mlsdevstore.presentaion.utils.Utils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

abstract class BaseViewModel : ViewModel(), LifecycleObserver {
    val compositeDisposable = CompositeDisposable()
    val messageLiveData = MutableLiveData<AlertMessage>()
    val technicalErrorLiveData = MutableLiveData<Boolean>()
    val networkErrorLiveData = MutableLiveData<Boolean>()
    val commonErrorLiveData = MutableLiveData<Boolean>()
    val authErrorLiveData = MutableLiveData<Boolean>()
    val isRefreshing = ObservableBoolean()
    val loadingStateLiveData = MutableLiveData<Boolean>()
    val viewModelJob = Job()
    val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    private val contentLoadingSubscription = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        viewModelScope.coroutineContext.cancel()
    }

    protected fun observeContentLoadingState(statePublisher: PublishSubject<DataLoadState>) {
        contentLoadingSubscription.add(statePublisher.subscribe {
            val loading = it == DataLoadState.LOADING
            isRefreshing.set(isRefreshing.get() and loading)
            loadingStateLiveData.postValue(loading)
        })
    }

    fun onTechnicalErrorOccurred() {
        technicalErrorLiveData.postValue(true)
        setIsRefreshing(false)
        setIsLoading(false)
    }

    fun onNetworkErrorOccurred() {
        networkErrorLiveData.postValue(true)
        setIsRefreshing(false)
        setIsLoading(false)
    }

    fun onCommonErrorOccurred() {
        commonErrorLiveData.postValue(true)
        setIsRefreshing(false)
        setIsLoading(false)
    }

    fun onAuthorizationErrorOccurred() {
        authErrorLiveData.postValue(true)
        setIsRefreshing(false)
        setIsLoading(false)
    }

    protected fun setIsRefreshing(isRefreshing: Boolean) {
        this.isRefreshing.set(isRefreshing)
        this.isRefreshing.notifyChange()
    }

    protected fun setIsLoading(isLoading: Boolean) {
        loadingStateLiveData.postValue(isLoading)
    }

    companion object {
        const val LOG_TAG = "ViewModel.Log.Tag"
        const val HTTP_ERROR_CODE_401 = 401
        const val HTTP_SERVER_ERROR_500 = 500
        const val HTTP_SERVER_ERROR_504 = 504
        const val OAUTH_ERROR_ID_1001 = 1001
        const val OAUTH_ERROR_ID_1100 = 1199
    }

    fun handleError(error: Throwable) {
        loadingStateLiveData.postValue(false)
        isRefreshing.set(false)

        when (error) {
            is ValidationException -> handleFieldsErrors(error)
            is SocketTimeoutException -> onNetworkErrorOccurred()
            is IOException -> onTechnicalErrorOccurred()
            is HttpException -> {
                when {
                    error.code() == HTTP_ERROR_CODE_401 -> onAuthorizationErrorOccurred()
                    error.code() in HTTP_SERVER_ERROR_500..HTTP_SERVER_ERROR_504 -> onTechnicalErrorOccurred()
                    error.response() != null && error.response().body() != null -> {
                        val errorsWrapper = ErrorParser().parse(error)
                        errorsWrapper.errors?.getOrNull(0)?.let { parsedError ->
                            Log.d(LOG_TAG, parsedError.message)
                            when (parsedError.errorId) {
                                in OAUTH_ERROR_ID_1001..OAUTH_ERROR_ID_1100 -> onAuthorizationErrorOccurred()
                                else -> onCommonErrorOccurred()
                            }
                        }
                        onCommonErrorOccurred()
                    }
                    else -> onCommonErrorOccurred()
                }
            }
            else -> onCommonErrorOccurred()
        }
    }

    protected open fun handleFieldsErrors(error: ValidationException) {

    }

    fun checkNetworkConnection(utils: Utils, onSuccessCallback: () -> Unit) {
        when (utils.isNetworkAvailable()) {
            true -> onSuccessCallback()
            else -> onNetworkErrorOccurred()
        }
    }

}
