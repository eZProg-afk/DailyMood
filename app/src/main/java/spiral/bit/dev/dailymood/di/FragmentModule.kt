package spiral.bit.dev.dailymood.di

import com.facebook.CallbackManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {

    @FragmentScoped
    @Provides
    fun provideCallbackManager(): CallbackManager = CallbackManager.Factory.create()
}