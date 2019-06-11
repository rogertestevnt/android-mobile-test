package com.mlsdev.mlsdevstore.injections.component


import com.mlsdev.mlsdevstore.MLSDevStoreApplication
import com.mlsdev.mlsdevstore.injections.module.*
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ActivityModule::class,
    ApplicationModule::class,
    FragmentBuilderModule::class,
    ViewModelModule::class,
    ApiModule::class,
    DatabaseModule::class,
    DataSourceModule::class
])
interface ApplicationComponent : AndroidInjector<MLSDevStoreApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MLSDevStoreApplication>()

}
