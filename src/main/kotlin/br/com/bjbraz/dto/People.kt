package br.com.bjbraz.dto

class People (

        var birthDate: String?=null,

        val name:String?=null,

        var cardName: String?=null,

        val type: String = "PF",

        val gender: String = "male",

        val companyName: String = "BJBraz desenvolvimento de Sistemas Ltda",

        val email:String?=null,

        val homePhone:String?=null,

        val businessPhone:String?=null,

        val mobilePhone:String?=null,

        val address:Address?=null,

        val identifierDocument:IdentifierDocument?=null,

        val documents:List<Document>?=null
)