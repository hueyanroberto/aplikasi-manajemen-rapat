package ac.id.ubaya.aplikasimanajemenrapat.core.di

import ac.id.ubaya.aplikasimanajemenrapat.core.data.repository.MeetingRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.data.repository.OrganizationRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.data.repository.UserPreferenceRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.data.repository.UserRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository.IMeetingRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository.IOrganizationRepository
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

    @Binds
    abstract fun provideOrganizationRepository(organizationRepository: OrganizationRepository): IOrganizationRepository

    @Binds
    abstract fun provideMeetingRepository(meetingRepository: MeetingRepository): IMeetingRepository
}