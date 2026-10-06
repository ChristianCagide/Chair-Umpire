package cagidech.chairumpire.domain.mapper

import cagidech.chairumpire.data.entity.ClientEntity
import cagidech.chairumpire.domain.model.Client
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Client.toClientEntity(): ClientEntity {
    return ClientEntity(
        id = id,
        name = name,
        phone = phone,
        isMember = isMember
    )
}

fun Flow<List<ClientEntity>>.toDomainClient(): Flow<List<Client>> {
    return this.map { clientEntities ->
        clientEntities.map { clientEntity ->
            clientEntity.toDomainClient()
        }
    }
}

private fun ClientEntity.toDomainClient(): Client {
    return Client(
        id = this.id,
        name = this.name,
        phone = this.phone,
        isMember = this.isMember
    )
}
