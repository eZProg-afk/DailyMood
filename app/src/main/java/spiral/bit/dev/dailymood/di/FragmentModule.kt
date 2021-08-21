package spiral.bit.dev.dailymood.di

import com.facebook.CallbackManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {

    @Provides
    fun provideCallbackManager() = CallbackManager.Factory.create()

}