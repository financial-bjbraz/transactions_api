package br.com.bjbraz.repository


import br.com.bjbraz.dto.RequestReponse
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RequestReponseRepository : ReactiveMongoRepository<RequestReponse, String> {


}