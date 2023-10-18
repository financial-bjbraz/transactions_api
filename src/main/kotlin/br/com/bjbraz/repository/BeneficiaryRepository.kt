package br.com.bjbraz.repository

import br.com.bjbraz.entity.docted.BeneficiaryEntity
import org.springframework.cache.annotation.CacheConfig
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
//@CacheConfig(cacheNames= ["beneficiary"])
interface BeneficiaryRepository : ReactiveMongoRepository<BeneficiaryEntity, String> {
    fun findByBeneficiaryNameAndOwnerDocument(name:String, cpf:String) : List<BeneficiaryEntity>

    fun findByBeneficiaryDocumentAndOwnerDocument(beneficiaryDocument:String, cpf:String) : List<BeneficiaryEntity>

    fun findByBeneficiaryMobileAndOwnerDocument(beneficiaryMobile:String, cpf:String) : List<BeneficiaryEntity>

    fun findByOwnerDocument(cpf:String) : List<BeneficiaryEntity>
}