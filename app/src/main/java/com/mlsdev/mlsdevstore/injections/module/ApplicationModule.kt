package com.mlsdev.mlsdevstore.injections.module

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mlsdev.mlsdevstore.MLSDevStoreApplication
import com.mlsdev.mlsdevstore.presentaion.utils.FieldsValidator
import com.mlsdev.mlsdevstore.presentaion.utils.Utils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class ApplicationModule {

    @Provides
    @Singleton
    fun provideContext(application: MLSDevStoreApplication): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .setLenient()
                .create()
    }

    @Provides
    fun provideFieldsValidator(context: Context): FieldsValidator {
        return FieldsValidator(context)
    }

    @Provides
    @Singleton
    fun providesUtils(context: Context): Utils {
        return Utils(context)
    }
}
