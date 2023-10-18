package br.com.bjbraz.repository

import br.com.bjbraz.entity.account.AccountEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface SQLAccountRepository : ReactiveMongoRepository<AccountEntity, String> {
    /**
     *
     * @param fok
     * @return
     */
    fun findByFinancialOperationKey(fok: String): Optional<AccountEntity?>

    fun findByResponsibleIdentifierDocumentDocument(document: String): Optional<AccountEntity?>

    //account!!.responsible!!.identifierDocument!!.document

}