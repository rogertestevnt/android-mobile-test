package com.mlsdev.mlsdevstore.injections.module

import android.content.Context
import androidx.room.Room
import com.mlsdev.mlsdevstore.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME).build()
    }

}
