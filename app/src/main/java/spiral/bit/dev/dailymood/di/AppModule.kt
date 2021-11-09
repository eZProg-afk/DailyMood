package spiral.bit.dev.dailymood.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import spiral.bit.dev.dailymood.data.AppDatabase
import spiral.bit.dev.dailymood.data.analytics.AnalyticsDao
import spiral.bit.dev.dailymood.data.analytics.AnalyticsRepository
import spiral.bit.dev.dailymood.data.mood.MoodDao
import spiral.bit.dev.dailymood.data.mood.MoodRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideMoodDao(appDatabase: AppDatabase) =
        appDatabase.emotionDao()

    @Singleton
    @Provides
    fun provideAnalyticsDao(appDatabase: AppDatabase) =
        appDatabase.analyticsDao()

    @Provides
    fun provideMoodRepository(moodDao: MoodDao) =
        MoodRepository(moodDao)

    @Provides
    fun provideAnalyticsRepository(analyticsDao: AnalyticsDao) =
        AnalyticsRepository(analyticsDao)

    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseAnalytics(@ApplicationContext context: Context) =
        FirebaseAnalytics.getInstance(context)
}