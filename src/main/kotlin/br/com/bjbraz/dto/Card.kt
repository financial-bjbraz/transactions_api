package br.com.bjbraz.dto

class Card (
        val externalCode:String?=null,
        val financialOperationKey:String?=null,
        val virtualCard:Boolean=true,
        val holder:Holder?=null,
        val delivery:Delivery?=null,
        val embossing:Embossing?=null
        )

