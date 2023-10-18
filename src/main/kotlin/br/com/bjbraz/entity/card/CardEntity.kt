package br.com.bjbraz.entity.card

import br.com.bjbraz.entity.account.AccountEntity
import lombok.AllArgsConstructor
import lombok.EqualsAndHashCode
import lombok.Getter
import lombok.Setter
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import java.util.*

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
class CardEntity (

        @Id
        val id: ObjectId? = null,

        val externalCode:String?=null,

        val financialOperationKey:String?=null,

        var orderNumber:String?=null,

        val virtualCard:Boolean=false,

        var status:CardStatus=CardStatus.REQUESTED,

        val holder: HolderEntity?=null,

        val delivery: DeliveryEntity?=null,

        val embossing: EmbossingEntity?=null,

        val account: AccountEntity?=null,

        var lastUpdated: Date?=null,

        var truncatedNumber:String?="",

        var cardId:Long?=0,

        var requestId:String?="",

        var externalCreatedAt:String?=null
)