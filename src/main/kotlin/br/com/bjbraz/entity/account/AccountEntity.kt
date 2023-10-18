package br.com.bjbraz.entity.account

import br.com.bjbraz.entity.card.AccountStatus
import br.com.bjbraz.entity.card.CardEntity
import com.fasterxml.jackson.annotation.JsonFilter
import lombok.Getter
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Getter
@JsonFilter("qrcodeFilter")
@Document(collection = "account")
class AccountEntity(

    @Id
    val id: String? = null,

    var owner: PeopleEntity? = null,

    var responsible: PeopleEntity? = null,

    val accountNumber: Long? = null,

    val dt_ref: Date? = null,

    val firebaseId: String? = null,

    var financialOperationKey: String? = null,

    var lastUpdated: Date? = null,

    var requestCard: Boolean = true,

    val cards: MutableSet<CardEntity>? = null,

    var status: AccountStatus = AccountStatus.CREATED,

    var documentPrincipal: String? = null

)