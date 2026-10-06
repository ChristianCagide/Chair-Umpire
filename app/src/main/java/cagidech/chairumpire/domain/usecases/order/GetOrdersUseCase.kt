package cagidech.chairumpire.domain.usecases.order

import cagidech.chairumpire.domain.mapper.toDomainClient
import cagidech.chairumpire.domain.mapper.toDomainOrder
import cagidech.chairumpire.domain.model.Order
import cagidech.chairumpire.domain.repository.ClientRepository
import cagidech.chairumpire.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val clientRepository: ClientRepository
) {
    operator fun invoke(): Flow<List<Order>> {
        val clientsFlow = clientRepository.getClients().toDomainClient()
        return orderRepository.getOrders().toDomainOrder(clientsFlow)
    }
}
