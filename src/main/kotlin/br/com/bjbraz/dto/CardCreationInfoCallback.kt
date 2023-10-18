package br.com.bjbraz.dto

import lombok.Data

@Data
class CardCreationInfoCallback (
        val truncatedNumber:String?="",
        val externalCode:String?="",
        val createdAt:String?="",
        val cardId:String?=""
)

/*
*
* "card":{
        "truncatedNumber":"478506******0866",
        "externalCode":"15862049545e8b911aea879",
        "createdAt":"2020-04-08T16:58:06-03:00",
        "cardId":"823135"}*/