package br.com.bjbraz.repository

import br.com.bjbraz.entity.auth.AuthEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthRepository : MongoRepository<AuthEntity, String> {

      fun findByDtRef(dtRef: String?): Optional<AuthEntity?>?

}