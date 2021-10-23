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
import spiral.bit.dev.dailymood.data.emotion.MoodDao
import spiral.bit.dev.dailymood.data.emotion.MoodRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideEmotionDao(appDatabase: AppDatabase) =
        appDatabase.emotionDao()

    @Provides
    fun provideEmotionRepository(moodDao: MoodDao) =
        MoodRepository(moodDao)

    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseAnalytics(@ApplicationContext context: Context) =
        FirebaseAnalytics.getInstance(context)
}