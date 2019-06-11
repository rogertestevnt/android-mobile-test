package com.mlsdev.mlsdevstore.data.repository

import androidx.lifecycle.MutableLiveData
import com.mlsdev.mlsdevstore.data.DataLoadState

abstract class BaseRepository {
    val loadingState = MutableLiveData<DataLoadState>()
}