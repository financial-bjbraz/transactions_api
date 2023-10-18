package br.com.bjbraz.dto

class AccountInfo(

        var id: Int = 0,
        val cpf:String?=null,
        val financialOperationKey: Int?,
        val mainAccountId: Int?=null,
        val balance: Double?=null,
        val name: String?=null,
        var temCartao:Boolean=true

)