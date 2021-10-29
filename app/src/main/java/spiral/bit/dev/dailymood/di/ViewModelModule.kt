package spiral.bit.dev.dailymood.di

import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import spiral.bit.dev.dailymood.ui.common.formatters.DateTimeFormatter
import spiral.bit.dev.dailymood.ui.common.mappers.EmotionTypeMapper
import spiral.bit.dev.dailymood.ui.common.resolvers.FaceMoodResolver
import spiral.bit.dev.dailymood.ui.common.resolvers.SurveyResolver
import spiral.bit.dev.dailymood.ui.feature.survey_add_mood.providers.AnswerProvider
import spiral.bit.dev.dailymood.ui.feature.survey_add_mood.providers.QuestionProvider
import spiral.bit.dev.dailymood.ui.feature.survey_add_mood.providers.ScoreProvider

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @ViewModelScoped
    @FaceDetectorOptionsRealtime
    @Provides
    fun provideFaceDetectorRealtimeOptions() = FaceDetectorOptions.Builder()
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .enableTracking()
        .build()

    @ViewModelScoped
    @FaceDetectorOptionsStatic
    @Provides
    fun provideFaceDetectorStaticOptions() = FaceDetectorOptions.Builder()
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    @ViewModelScoped
    @FaceDetectorOptionsStatic
    @Provides
    fun provideStaticFaceDetector(@FaceDetectorOptionsStatic faceDetectorOptions: FaceDetectorOptions) =
        FaceDetection.getClient(faceDetectorOptions)

    @ViewModelScoped
    @FaceDetectorOptionsRealtime
    @Provides
    fun provideRealtimeFaceDetector(@FaceDetectorOptionsRealtime faceDetectorOptions: FaceDetectorOptions) =
        FaceDetection.getClient(faceDetectorOptions)

    @ViewModelScoped
    @Provides
    fun provideEmotionTypeMapper() = EmotionTypeMapper()

    @ViewModelScoped
    @Provides
    fun provideFaceMoodResolver() = FaceMoodResolver()

    @ViewModelScoped
    @Provides
    fun provideSurveyResolver() = SurveyResolver()

    @ViewModelScoped
    @Provides
    fun provideDateTimeFormatter() = DateTimeFormatter()

    @ViewModelScoped
    @Provides
    fun provideQuestionProvider() = QuestionProvider()

    @ViewModelScoped
    @Provides
    fun provideAnswerProvider() = AnswerProvider()

    @ViewModelScoped
    @Provides
    fun provideScoreProvider() = ScoreProvider()
}