package cagidech.chairumpire.domain.usecases.order

import cagidech.chairumpire.domain.DataError
import cagidech.chairumpire.domain.Result
import cagidech.chairumpire.domain.mapper.toOrderEntity
import cagidech.chairumpire.domain.model.Order
import cagidech.chairumpire.domain.repository.OrderRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpsertOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) {

    suspend operator fun invoke(order: Order): Result<Unit, DataError.Local> {
        return orderRepository.upsertOrder(order.toOrderEntity())
    }
}
