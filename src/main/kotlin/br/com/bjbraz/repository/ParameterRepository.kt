package br.com.bjbraz.repository

import br.com.bjbraz.entity.general.ParameterEntity
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
//@CacheConfig(cacheNames= ["params"])
interface ParameterRepository : ReactiveMongoRepository<ParameterEntity, Long?> {

    @Cacheable(key="#name", value= ["params"])
    fun findByName(name: String): Optional<ParameterEntity>
}