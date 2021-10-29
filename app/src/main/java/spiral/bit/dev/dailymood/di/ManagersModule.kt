package spiral.bit.dev.dailymood.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import spiral.bit.dev.dailymood.ui.feature.user.managers.UserManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagersModule {

    @Singleton
    @Provides
    fun provideUserManager(auth: FirebaseAuth) = UserManager(auth)
}