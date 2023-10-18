package br.com.bjbraz.repository

import br.com.bjbraz.entity.card.CardEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SQLCardRepository : ReactiveMongoRepository<CardEntity, Long?> {
    /**
     *
     * @param externalCode
     * @return
     */
    fun findByExternalCode(externalCode: String): Optional<CardEntity>

    fun findByOrderNumber(orderNumber: String): Optional<CardEntity>

}