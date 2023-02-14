package ac.id.ubaya.aplikasimanajemenrapat.core.di

import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.local.room.ManajemenRapatDatabase
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.local.room.OrganizationDao
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ManajemenRapatDatabase = Room.databaseBuilder(
        context,
        ManajemenRapatDatabase::class.java, "ManajemenRapat.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideOrganizationDao(database: ManajemenRapatDatabase): OrganizationDao = database.organizationDao()
}