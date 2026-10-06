package cagidech.chairumpire.domain.repository

import cagidech.chairumpire.data.entity.ClientEntity
import cagidech.chairumpire.domain.DataError
import cagidech.chairumpire.domain.Result
import kotlinx.coroutines.flow.Flow

interface ClientRepository {

    fun getClients(): Flow<List<ClientEntity>>

    suspend fun upsertClient(client: ClientEntity): Result<Unit, DataError.Local>

    suspend fun deleteClient(client: ClientEntity): Result<Unit, DataError.Local>
}