package br.com.bjbraz.dto

class Holder (
        val name:String?=null,
        val document:String?=null,
        val email:String?=null,
        val mobilePhone:String?=null,
        val homePhone:String?=null,
        var birthDate: String?=null,
        val gender: String="male",
        val address:Address?=null
)