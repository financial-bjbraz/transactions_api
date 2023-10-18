package br.com.bjbraz.dto

import lombok.Data

@Data
class CardCreationCallback (
        val requestId:String?=null,
        val financialOperationKey:String?=null,
        val success:Boolean=true,
        val card: CardCreationInfoCallback?=null,
        var _embedded:CardCreationEmbedded?=null


/*
{"requestId":"fc5e134f-dcdb-4a74-bd42-7f9b678c18fe","success":true,
    "financialOperationKey":"4423400",
    "card":{
        "truncatedNumber":"478506******0866",
        "externalCode":"15862049545e8b911aea879",
        "createdAt":"2020-04-08T16:58:06-03:00",
        "cardId":"823135"}
        "_embedded":{"errors":[{"logref":"REQUIRED","message":"'Address. Number' should not be empty.","path":"/accounts[0]/cards[0]/delivery/content/recipient/address/number"}]}}
}
*/

)