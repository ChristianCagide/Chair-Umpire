package cagidech.chairumpire.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import cagidech.chairumpire.data.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Upsert
    suspend fun upsertOrder(orderEntity: OrderEntity)

    @Query("SELECT * FROM orderentity")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Delete
    suspend fun deleteOrder(orderEntity: OrderEntity)
}
