package br.com.bjbraz.dto

import lombok.Data

@Data
class CardReissueCallbackItem(
        var cardId:Long?=null,
        var orderId:Long?=null,
        var externalId:String?=null,
        var externalCode:String?=null,
        var truncatedNumber:String?=null )

    //"cardId":823256,"orderId":165005,"externalId":"15868253425e95087eb8ec5","externalCode":"15868253425e95087eb8ec5","truncatedNumber":"478506******1773"
