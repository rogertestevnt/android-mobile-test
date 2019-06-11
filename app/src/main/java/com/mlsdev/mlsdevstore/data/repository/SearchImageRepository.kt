package com.mlsdev.mlsdevstore.data.repository

import com.mlsdev.mlsdevstore.data.applyDefaultSchedulers
import com.mlsdev.mlsdevstore.data.local.database.AppDatabase
import com.mlsdev.mlsdevstore.data.model.image.CategoryImageEntity
import com.mlsdev.mlsdevstore.data.remote.service.SearchImageService
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class SearchImageRepository @Inject constructor(
        private val searchImageService: SearchImageService,
        private val database: AppDatabase
) : BaseRepository() {

    open fun searchImage(categoryId: String, categoryName: String): Single<CategoryImageEntity> =
            database.categoryImagesDao().queryCategoryImageEntity(categoryId)
                    .applyDefaultSchedulers()
                    .flatMap {
                        return@flatMap if (it.isEmpty() || (it.isNotEmpty() && it[0].imageUrl.isNullOrBlank())) {
                            var name = Regex("[^A-Za-z ]").replace(categoryName.replace("/", " "), "").toLowerCase()
                            val words = name.split(' ')
                            if (words.size >= 2) name = "${words[0]} ${words[1]}"

                            searchImageService.searchImage(name)
                                    .applyDefaultSchedulers()
                                    .flatMap { page ->
                                        val categoryImage = CategoryImageEntity(categoryId, page.getImageUrl())
                                        Completable.fromCallable { database.categoryImagesDao().insert(categoryImage) }
                                                .subscribeOn(Schedulers.io())
                                                .subscribe()
                                        Single.just(categoryImage)
                                    }
                        } else {
                            Single.just(it[0])
                        }
                    }

}