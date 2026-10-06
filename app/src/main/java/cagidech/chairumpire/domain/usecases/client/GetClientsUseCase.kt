package cagidech.chairumpire.domain.usecases.client

import cagidech.chairumpire.domain.mapper.toDomainClient
import cagidech.chairumpire.domain.model.Client
import cagidech.chairumpire.domain.repository.ClientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClientsUseCase @Inject constructor(private val repository: ClientRepository) {
    operator fun invoke(): Flow<List<Client>> {
        return repository.getClients().toDomainClient()
    }
}