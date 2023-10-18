package br.com.bjbraz.repository

import br.com.bjbraz.entity.boleto.BoletoEntity
import br.com.bjbraz.entity.docted.DocTedEntity
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
//@CacheConfig(cacheNames= ["docted"])
interface DocTedRepository : ReactiveMongoRepository<DocTedEntity, Long?> {

    fun findByExternalId(externalId: String): Optional<DocTedEntity>

    fun findByTransactionNumber(transactionNumber: String): Optional<DocTedEntity>

}