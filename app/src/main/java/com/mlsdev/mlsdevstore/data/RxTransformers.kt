package com.mlsdev.mlsdevstore.data

import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

fun <T> Flowable<T>.applyDefaultSchedulers(): Flowable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.applyDefaultSchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.applyDefaultSchedulers(): Single<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun Completable.applyDefaultSchedulers(): Completable {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.handleLoading(loadingPublisher: PublishSubject<DataLoadState>): Observable<T> {
    return doOnSubscribe { loadingPublisher.onNext(DataLoadState.LOADING) }
            .doOnTerminate { loadingPublisher.onNext(DataLoadState.LOADED) }
            .doOnError { loadingPublisher.onNext(DataLoadState.FAILED) }
            .doOnNext { loadingPublisher.onNext(DataLoadState.LOADED) }
}

fun <T> Single<T>.handleLoading(loadingPublisher: PublishSubject<DataLoadState>): Single<T> {
    return doOnSubscribe { loadingPublisher.onNext(DataLoadState.LOADING) }
            .doOnError { loadingPublisher.onNext(DataLoadState.FAILED) }
            .doOnSuccess { loadingPublisher.onNext(DataLoadState.LOADED) }
}

fun <T> Single<T>.handleLoading(loadingPublisher: MutableLiveData<DataLoadState>): Single<T> {
    return doOnSubscribe { loadingPublisher.postValue(DataLoadState.LOADING) }
            .doOnError { loadingPublisher.postValue(DataLoadState.FAILED) }
            .doOnSuccess { loadingPublisher.postValue(DataLoadState.LOADED) }
}

fun Completable.handleLoading(loadingPublisher: PublishSubject<DataLoadState>): Completable {
    return doOnSubscribe { loadingPublisher.onNext(DataLoadState.LOADING) }
            .doOnTerminate { loadingPublisher.onNext(DataLoadState.LOADED) }
            .doOnError { loadingPublisher.onNext(DataLoadState.FAILED) }
            .doOnComplete { loadingPublisher.onNext(DataLoadState.LOADED) }
}
