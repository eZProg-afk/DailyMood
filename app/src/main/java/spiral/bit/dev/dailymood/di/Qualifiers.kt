package spiral.bit.dev.dailymood.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FaceDetectorOptionsRealtime

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FaceDetectorOptionsStatic
