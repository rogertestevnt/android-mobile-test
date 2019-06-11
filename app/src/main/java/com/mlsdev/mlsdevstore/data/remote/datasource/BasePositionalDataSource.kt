package com.mlsdev.mlsdevstore.data.remote.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import com.mlsdev.mlsdevstore.data.DataLoadState
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action

const val PAGE_SIZE = 10

fun getPagingConfig(): PagedList.Config = PagedList.Config.Builder()
        .setPageSize(PAGE_SIZE)
        .setEnablePlaceholders(false)
        .build()

abstract class BasePositionalDataSource<T> : PositionalDataSource<T>() {
    protected var retryCompletable: Completable? = null
    protected var disposable: Disposable? = null
    protected var totalCount = 0
    val loadStateLiveData = MutableLiveData<DataLoadState>()

    fun retry() {
        retryCompletable?.let {
            setRetry(null)
            return@let it.observeOn(AndroidSchedulers.mainThread()).subscribe(
                    { },
                    { throwable -> Log.d("debugDS", "error: " + throwable.message) })
        }
    }

    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)
        }
    }

    protected fun handleError(exp: Throwable, params: LoadInitialParams, callback: LoadInitialCallback<T>) {
        // keep a Completable for future retry
        setRetry(Action { loadInitial(params, callback) })
        // publish the error
        loadStateLiveData.postValue(DataLoadState.FAILED)
        exp.printStackTrace()
    }

    protected fun handleError(exp: Throwable, params: LoadRangeParams, callback: LoadRangeCallback<T>) {
        // keep a Completable for future retry
        setRetry(Action { loadRange(params, callback) })
        // publish the error
        loadStateLiveData.postValue(DataLoadState.FAILED)
        exp.printStackTrace()
    }

    open fun getDataLoadState() : LiveData<DataLoadState> = loadStateLiveData
}