package cagidech.chairumpire.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import cagidech.chairumpire.data.entity.ClientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {

    @Upsert
    suspend fun upsertClient(clientEntity: ClientEntity)

    @Query("SELECT * FROM cliententity ORDER BY name ASC")
    fun getClientsOrderedByFirstName(): Flow<List<ClientEntity>>

    @Query("SELECT * FROM cliententity WHERE id = :clientId")
    suspend fun getClientById(clientId: Long): ClientEntity

    @Delete
    suspend fun deleteClient(clientEntity: ClientEntity)
}