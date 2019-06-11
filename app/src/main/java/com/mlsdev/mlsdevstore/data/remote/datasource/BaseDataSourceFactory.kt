package com.mlsdev.mlsdevstore.data.remote.datasource

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

abstract class BaseDataSourceFactory<Key, Value> : DataSource.Factory<Key, Value>() {

    open val dataSourceLiveData = MutableLiveData<DataSource<Key, Value>>()

    open fun invalidateDataSource() {
        dataSourceLiveData.value?.invalidate()
    }

    abstract fun retry()

    abstract fun applyArguments(bundle: Bundle)

}