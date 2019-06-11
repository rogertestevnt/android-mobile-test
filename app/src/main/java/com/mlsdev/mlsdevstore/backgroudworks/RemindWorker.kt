package com.mlsdev.mlsdevstore.backgroudworks

import android.content.Context
import androidx.room.Room
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.mlsdev.mlsdevstore.data.local.database.AppDatabase
import com.mlsdev.mlsdevstore.notifications.AppNotificationManager
import io.reactivex.Single
import kotlin.random.Random

class RemindWorker(context: Context, workerParams: WorkerParameters) : RxWorker(context, workerParams) {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME).build()
    }

    private val appNotificationManager = AppNotificationManager(applicationContext)

    override fun createWork(): Single<Result> {
        return database.categoriesDao().queryCategoryTreeNode()
                .map { it[Random.nextInt(0, it.size)] }
                .doOnSuccess { appNotificationManager.sendNotification(it) }
                .map { Result.success() }
                .onErrorReturn { Result.failure() }
    }

}