package br.com.bjbraz.repository

import br.com.bjbraz.entity.docted.BankAccountTypeEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
//@CacheConfig(cacheNames= ["banktype"])
interface BankTypeRepository : ReactiveMongoRepository<BankAccountTypeEntity, Long?> {
    fun findByName(name:String) : Optional<BankAccountTypeEntity>

}