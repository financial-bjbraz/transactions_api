package br.com.bjbraz.dto

import java.io.Serializable

open class BoletoRequest(
        val digitableLine:String,
        val amount:Double,
        val summary:String? = null,
        val cpf:String,
        val financialOperationKey:String?,
        var externalId:String?,
        var scheduleDate:String? = null,
        var infoBoleto:BoletoRequestInfoBoleto? = null) : Serializable