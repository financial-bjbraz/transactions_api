package br.com.bjbraz.entity.boleto

import br.com.bjbraz.entity.account.AccountEntity
import br.com.bjbraz.entity.card.BoletoStatus
import lombok.Getter
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Getter
@Document(collection = "account")
class BoletoEntity (
        @Id
    val id:String? = null,

    val dt_ref: Date?=null,

    var status: BoletoStatus = BoletoStatus.CREATED,

    var externalId:String?=null,

    var amount:Double?=null,

    var summary:String?=null,

    var scheduleDate:Date?=null,

    var digitableLine:String?=null,

    var dueDate:Date?=null,

    var account: AccountEntity?=null,

    var financialOperationKey: String?=null,

    var document: String?=null
)