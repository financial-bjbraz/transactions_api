package br.com.bjbraz.entity.docted

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(value = "doc_ted_entity")
class DocTedEntity (
        @Id
        val id:String? = null,

        val externalId:String? = null,

        val amount:Double? = null,

        val type:DocTedType? = null,

        val summary:String? = null,

        val scheduleDate: Date? = null,

        val requestDate: Date? = null,

        var bankAccountTypeEntity: BankAccountTypeEntity?=null,

        var bank: BankEntity?=null,

        var beneficiaryEntity: BeneficiaryEntity?=null,

        var status:String? = null,

        val transactionNumber:String? = null,

        var document: String?=null

)