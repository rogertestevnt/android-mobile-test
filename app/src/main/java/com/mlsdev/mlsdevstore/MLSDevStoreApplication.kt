package com.mlsdev.mlsdevstore

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.mlsdev.mlsdevstore.backgroudworks.RemindWorker
import com.mlsdev.mlsdevstore.injections.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import java.util.concurrent.TimeUnit

open class MLSDevStoreApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        initBackgroundWorkers()
    }

    private fun initBackgroundWorkers() {
        val constrains = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val reminderWork = PeriodicWorkRequest.Builder(RemindWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(constrains)
                .build()

        WorkManager.getInstance().enqueue(reminderWork)
    }

}
