package br.com.bjbraz.entity.docted

import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(value = "beneficiary")
@Data
data class BeneficiaryEntity(

    @Id
    val id: String? = null,

    val beneficiaryName: String? = null,

    var branch: String? = null,//agencia

    val account: String? = null,

    val accountDigit: String? = null,

    val beneficiaryDocument: String? = null,

    val beneficiaryMobile: String? = null,

    var bankAccountType: BankAccountTypeEntity? = null,

    var bank: BankEntity? = null,

    var ownerDocument: String? = null

)