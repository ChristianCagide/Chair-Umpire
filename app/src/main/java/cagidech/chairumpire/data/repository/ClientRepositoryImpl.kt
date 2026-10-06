package cagidech.chairumpire.data.repository

import android.database.sqlite.SQLiteOutOfMemoryException
import android.util.Log
import androidx.sqlite.SQLiteException
import cagidech.chairumpire.data.dao.ClientDao
import cagidech.chairumpire.data.entity.ClientEntity
import cagidech.chairumpire.domain.DataError
import cagidech.chairumpire.domain.Result
import cagidech.chairumpire.domain.repository.ClientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(val clientDao: ClientDao) : ClientRepository {

    override fun getClients(): Flow<List<ClientEntity>> {
        return clientDao.getClientsOrderedByFirstName()
    }

    override suspend fun upsertClient(client: ClientEntity): Result<Unit, DataError.Local> {
        return try {
            clientDao.upsertClient(client)
            Result.Success(Unit)
        } catch (e: SQLiteOutOfMemoryException) {
            Log.e("TAG", "upsertClient: $e")
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: SQLiteException) {
            Log.e("TAG", "upsertClient: $e")
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteClient(client: ClientEntity): Result<Unit, DataError.Local> {
        return try {
            clientDao.deleteClient(client)
            Result.Success(Unit)
        } catch (e: SQLiteOutOfMemoryException) {
            Log.e("TAG", "deleteClient: $e")
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: SQLiteException) {
            Log.e("TAG", "deleteClient: $e")
            Result.Error(DataError.Local.UNKNOWN)
        } catch (e: Exception) {
            Log.e("TAG", "deleteClient: $e")
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}