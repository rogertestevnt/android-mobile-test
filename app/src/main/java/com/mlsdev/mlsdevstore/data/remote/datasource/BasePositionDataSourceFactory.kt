package com.mlsdev.mlsdevstore.data.remote.datasource

import android.os.Bundle
import androidx.lifecycle.LiveData

abstract class BasePositionDataSourceFactory<Key, Value> : BaseDataSourceFactory<Key, Value>() {

    @Suppress("UNCHECKED_CAST")
    open fun getDataSourceLiveData(): LiveData<BasePositionalDataSource<Value>> =
            dataSourceLiveData as LiveData<BasePositionalDataSource<Value>>

    override fun retry() {
        dataSourceLiveData.value?.let {
            if (it is BasePositionalDataSource)
                it.retry()
        }
    }

    override fun applyArguments(bundle: Bundle) {

    }
}