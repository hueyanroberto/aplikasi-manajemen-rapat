package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.local.room

import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.local.entity.OrganizationEntity
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface OrganizationDao {
    @Query("SELECT * FROM organizations")
    fun getOrganizations(): Flow<List<OrganizationEntity>>

    @Query("DELETE FROM organizations")
    suspend fun deleteAll()

    @Update
    suspend fun update(organizations: OrganizationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(organizations: List<OrganizationEntity>)
}