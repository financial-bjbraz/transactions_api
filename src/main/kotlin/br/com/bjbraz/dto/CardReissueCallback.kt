package br.com.bjbraz.dto

import lombok.Data

@Data
class CardReissueCallback (
        var requestId:String?=null,
        var success:Boolean?=true,
        var originalCard:CardReissueCallbackItem?=null,
        var reissuedCard:CardReissueCallbackItem?=null,
        var idempotencyKey:String?=null)

/*
{"requestId":"15868253425e95087eb8ec5",
"success":true,
"originalCard":{"cardId":823223,"externalId":"15868253425e95087eb8ec5","externalCode":"15868253425e95087eb8ec5","truncatedNumber":"478506******1476"},
"reissuedCard":{"cardId":823256,"orderId":165005,"externalId":"15868253425e95087eb8ec5","externalCode":"15868253425e95087eb8ec5","truncatedNumber":"478506******1773"},
"idempotencyKey":"cd2731cd-73b4-49b4-830c-58aca555ab7e"}
*/

