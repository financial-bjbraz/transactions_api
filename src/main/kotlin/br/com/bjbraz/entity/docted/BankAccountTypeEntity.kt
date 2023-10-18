package br.com.bjbraz.entity.docted

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(value = "bank_account_entity")
class BankAccountTypeEntity(

        @Id
        val id:String? = null,

        val name:String? = null,

        val description:String? = null
)