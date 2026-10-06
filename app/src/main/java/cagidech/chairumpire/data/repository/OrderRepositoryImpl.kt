package cagidech.chairumpire.data.repository

import android.database.sqlite.SQLiteOutOfMemoryException
import android.util.Log
import androidx.sqlite.SQLiteException
import cagidech.chairumpire.data.dao.OrderDao
import cagidech.chairumpire.data.entity.OrderEntity
import cagidech.chairumpire.domain.DataError
import cagidech.chairumpire.domain.Result
import cagidech.chairumpire.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao
) : OrderRepository {

    override fun getOrders(): Flow<List<OrderEntity>> {
        return orderDao.getAllOrders()
    }

    override suspend fun upsertOrder(orderEntity: OrderEntity): Result<Unit, DataError.Local> {
        return try {
            orderDao.upsertOrder(orderEntity)
            Result.Success(Unit)
        } catch (e: SQLiteOutOfMemoryException) {
            Log.e("OrderRepository", "upsertOrder: $e")
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: SQLiteException) {
            Log.e("OrderRepository", "upsertOrder: $e")
            Result.Error(DataError.Local.UNKNOWN)
        } catch (e: Exception) {
            Log.e("OrderRepository", "upsertOrder: $e")
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteOrder(orderEntity: OrderEntity): Result<Unit, DataError.Local> {
        return try {
            orderDao.deleteOrder(orderEntity)
            Result.Success(Unit)
        } catch (e: SQLiteOutOfMemoryException) {
            Log.e("OrderRepository", "deleteOrder: $e")
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: SQLiteException) {
            Log.e("OrderRepository", "deleteOrder: $e")
            Result.Error(DataError.Local.UNKNOWN)
        } catch (e: Exception) {
            Log.e("OrderRepository", "deleteOrder: $e")
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
