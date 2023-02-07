package ac.id.ubaya.aplikasimanajemenrapat.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "ac.id.ubaya.aplikasimanajemenrapat.userPreferences"
)

@Module
@InstallIn(SingletonComponent::class)
class UserPreferenceModule {

    @Provides
    @Singleton
    fun provideUserDataStorePreference(
        @ApplicationContext applicationContext: Context
    ): DataStore<Preferences> {
        return applicationContext.userDataStore
    }
}