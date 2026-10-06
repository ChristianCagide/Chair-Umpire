package cagidech.chairumpire.domain.usecases.client

import cagidech.chairumpire.domain.DataError
import cagidech.chairumpire.domain.Result
import cagidech.chairumpire.domain.mapper.toClientEntity
import cagidech.chairumpire.domain.model.Client
import cagidech.chairumpire.domain.repository.ClientRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpsertClientUseCase @Inject constructor(private val clientRepository: ClientRepository) {

    suspend operator fun invoke(client: Client): Result<Unit, DataError.Local> {
        return clientRepository.upsertClient(client.toClientEntity())
    }
}