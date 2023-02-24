package ac.id.ubaya.aplikasimanajemenrapat.di

import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.meeting.MeetingInteractor
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.meeting.MeetingUseCase
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.organization.OrganizationInteractor
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.organization.OrganizationUseCase
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.user.UserInteractor
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.user.UserUseCase
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.userPreference.UserPreferenceInteractor
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.userPreference.UserPreferenceUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {
    @Binds
    abstract fun provideUserUseCase(userInteractor: UserInteractor): UserUseCase

    @Binds
    abstract fun provideUserPreferenceUseCase(userPreferenceInteractor: UserPreferenceInteractor): UserPreferenceUseCase

    @Binds
    abstract fun provideOrganizationUseCase(organizationInteractor: OrganizationInteractor): OrganizationUseCase

    @Binds
    abstract fun provideMeetingUseCase(meetingInteractor: MeetingInteractor): MeetingUseCase
}