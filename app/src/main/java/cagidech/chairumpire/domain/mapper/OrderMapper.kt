package cagidech.chairumpire.domain.mapper

import cagidech.chairumpire.data.entity.OrderEntity
import cagidech.chairumpire.domain.model.Client
import cagidech.chairumpire.domain.model.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

fun Order.toOrderEntity(): OrderEntity {
    return OrderEntity(
        clientId = client.id,
        racketType = racketType,
        stringType = stringType,
        tension = tension,
        dropOffDateTime = dropOffDateTime,
        pickUpDateTime = pickUpDateTime,
        stringerName = stringerName,
        dateBilled = dateBilled
    )
}

fun Flow<List<OrderEntity>>.toDomainOrder(clientsFlow: Flow<List<Client>>): Flow<List<Order>> {
    return combine(this, clientsFlow) { orderEntities, clients ->
        orderEntities.map { orderEntity ->
            orderEntity.toDomainOrder(clients)
        }
    }
}

private fun OrderEntity.toDomainOrder(clients: List<Client>): Order {
    val matchedClient = clients.find { it.id == clientId } ?: Client(
        id = clientId,
        name = "Unknown",
        phone = "",
        isMember = false
    )
    return Order(
        id = id,
        client = matchedClient,
        racketType = racketType,
        stringType = stringType,
        tension = tension,
        dropOffDateTime = dropOffDateTime,
        pickUpDateTime = pickUpDateTime,
        stringerName = stringerName,
        dateBilled = dateBilled
    )
}
