package spiral.bit.dev.dailymood.di

import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideFaceDetectorOptions() = FaceDetectorOptions.Builder()
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .enableTracking()
        .build()

    @Provides
    fun provideFaceDetector(faceDetectorOptions: FaceDetectorOptions) =
        FaceDetection.getClient(faceDetectorOptions)
}