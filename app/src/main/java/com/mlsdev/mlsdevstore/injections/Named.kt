package com.mlsdev.mlsdevstore.injections

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Named(val value: String = "")