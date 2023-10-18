package br.com.bjbraz.repository

import br.com.bjbraz.entity.card.CardEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CardRepository : ReactiveMongoRepository<CardEntity, String> {


}