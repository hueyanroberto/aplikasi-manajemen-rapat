package ac.id.ubaya.aplikasimanajemenrapat.core.di

import ac.id.ubaya.aplikasimanajemenrapat.core.data.repository.UserPreferenceRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.data.repository.UserRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository.IUserPreferenceRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideUserRepository(userRepository: UserRepository): IUserRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        userPreferenceRepository: UserPreferenceRepository
    ): IUserPreferenceRepository
}