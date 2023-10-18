package br.com.bjbraz.repository

import br.com.bjbraz.entity.auth.AuthEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SQLAuthRepository : ReactiveMongoRepository<AuthEntity, String> {

    fun findByDtRef(dtRef: String): Optional<AuthEntity?>
}