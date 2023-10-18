package br.com.bjbraz.repository

import br.com.bjbraz.entity.boleto.BoletoEntity
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
//@CacheConfig(cacheNames= ["boleto"])
interface BoletoRepository : ReactiveMongoRepository<BoletoEntity, Long?> {

    @Cacheable(key="#externalId", value= ["boleto"])
    fun findByExternalId(externalId: String): Optional<BoletoEntity>
}