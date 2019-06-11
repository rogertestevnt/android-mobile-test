package com.mlsdev.mlsdevstore.injections.module

import com.mlsdev.mlsdevstore.injections.component.scope.ActivityScope
import com.mlsdev.mlsdevstore.presentaion.AppActivity
import com.mlsdev.mlsdevstore.presentaion.splashscreen.SplashScreenActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    @ActivityScope
    fun contributeAppActivity(): AppActivity

    @ContributesAndroidInjector
    @ActivityScope
    fun contributeSplashScreenActivity(): SplashScreenActivity

}
