package ac.id.ubaya.aplikasimanajemenrapat.core.data.repository

import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository.IUserPreferenceRepository
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferenceRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
): IUserPreferenceRepository {
    override fun getUser(): Flow<User> {
        return dataStore.data.map { preference ->
            User(
                preference[ID_KEY] ?: -1,
                preference[EMAIL_KEY] ?: "",
                preference[NAME_KEY] ?: "",
                preference[EXP_KEY] ?: 0,
                preference[PROFILE_PIC_KEY] ?: "",
                preference[LEVEL_ID_KEY] ?: -1
            )
        }
    }

    override suspend fun saveUser(user: User) {
        dataStore.edit { preference ->
            preference[ID_KEY] = user.id
            preference[EMAIL_KEY] = user.email
            preference[NAME_KEY] = user.name
            preference[EXP_KEY] = user.exp
            preference[PROFILE_PIC_KEY] = user.profilePic ?: ""
            preference[LEVEL_ID_KEY] = user.levelId
        }
    }

    override suspend fun logout() {
        dataStore.edit { preference ->
            preference[ID_KEY] = -1
            preference[EMAIL_KEY] = ""
            preference[NAME_KEY] = ""
            preference[EXP_KEY] = 0
            preference[PROFILE_PIC_KEY] = "'"
            preference[LEVEL_ID_KEY] = -1
        }
    }

    companion object {
        private val ID_KEY = intPreferencesKey("id")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val NAME_KEY = stringPreferencesKey("name")
        private val PROFILE_PIC_KEY = stringPreferencesKey("profile_pic")
        private val LEVEL_ID_KEY = intPreferencesKey("level_id")
        private val EXP_KEY = intPreferencesKey("exp")
    }
}