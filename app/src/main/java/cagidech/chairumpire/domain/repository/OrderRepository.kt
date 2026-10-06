package cagidech.chairumpire.domain.repository

import cagidech.chairumpire.data.entity.OrderEntity
import cagidech.chairumpire.domain.DataError
import cagidech.chairumpire.domain.Result
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    fun getOrders(): Flow<List<OrderEntity>>

    suspend fun upsertOrder(orderEntity: OrderEntity): Result<Unit, DataError.Local>

    suspend fun deleteOrder(orderEntity: OrderEntity): Result<Unit, DataError.Local>
}
