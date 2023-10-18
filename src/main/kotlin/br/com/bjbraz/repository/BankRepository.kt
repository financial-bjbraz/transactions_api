package br.com.bjbraz.repository

import br.com.bjbraz.entity.docted.BankEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
//@CacheConfig(cacheNames= ["bank"])
interface BankRepository : ReactiveMongoRepository<BankEntity, String> {
    fun findByNumber(number:Long) : Mono<BankEntity>
}