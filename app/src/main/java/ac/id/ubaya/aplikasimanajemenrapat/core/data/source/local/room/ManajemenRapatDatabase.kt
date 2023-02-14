package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.local.room

import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.local.entity.OrganizationEntity
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [OrganizationEntity::class], version = 1, exportSchema = true)
abstract class ManajemenRapatDatabase: RoomDatabase() {
    abstract fun organizationDao(): OrganizationDao

}