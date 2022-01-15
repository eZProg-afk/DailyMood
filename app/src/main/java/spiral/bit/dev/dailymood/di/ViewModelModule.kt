package spiral.bit.dev.dailymood.di

import android.content.Context
import com.facebook.CallbackManager
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import spiral.bit.dev.dailymood.ui.common.adapter.mappers.AdapterTypeMapper
import spiral.bit.dev.dailymood.ui.common.formatters.AppDateTimeFormatter
import spiral.bit.dev.dailymood.ui.common.mappers.MoodTypeMapper
import spiral.bit.dev.dailymood.ui.common.resolvers.AnalyticsResolver
import spiral.bit.dev.dailymood.ui.common.resolvers.FaceMoodResolver
import spiral.bit.dev.dailymood.ui.common.resolvers.SurveyResolver
import spiral.bit.dev.dailymood.ui.feature.analytics.providers.AnalyticsProvider
import spiral.bit.dev.dailymood.ui.feature.creationMood.surveyAddMood.providers.AnswerProvider
import spiral.bit.dev.dailymood.ui.feature.creationMood.surveyAddMood.providers.QuestionProvider
import spiral.bit.dev.dailymood.ui.feature.creationMood.surveyAddMood.providers.ScoreProvider

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
    fun provideEmotionTypeMapper() = MoodTypeMapper()

    @ViewModelScoped
    @Provides
    fun provideFaceMoodResolver() = FaceMoodResolver()

    @ViewModelScoped
    @Provides
    fun provideSurveyResolver() = SurveyResolver()

    @ViewModelScoped
    @Provides
    fun provideAnalyticsResolver() = AnalyticsResolver()

    @ViewModelScoped
    @Provides
    fun provideDateTimeFormatter() = AppDateTimeFormatter()

    @ViewModelScoped
    @Provides
    fun provideQuestionProvider() = QuestionProvider()

    @ViewModelScoped
    @Provides
    fun provideAnswerProvider() = AnswerProvider()

    @ViewModelScoped
    @Provides
    fun provideScoreProvider() = ScoreProvider()

    @ViewModelScoped
    @Provides
    fun provideAnalyticsProvider(
        adapterTypeMapper: AdapterTypeMapper,
        analyticsResolver: AnalyticsResolver,
        moodTypeMapper: MoodTypeMapper,
        @ApplicationContext context: Context
    ) = AnalyticsProvider(adapterTypeMapper, analyticsResolver, moodTypeMapper, context)

    @ViewModelScoped
    @Provides
    fun provideAdapterTypeMapper() = AdapterTypeMapper()

    @ViewModelScoped
    @Provides
    fun provideCallbackManager(): CallbackManager = CallbackManager.Factory.create()
}