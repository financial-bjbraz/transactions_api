package br.com.bjbraz.dto

import lombok.Data
import java.io.Serializable

@Data
class Boleto(
        var digitableLine: String?=null,
        var amount:Double?= null,
        var summary:String? = null,
        var cpf:String? = null,
        var financialOperationKey:String?=null,
        var scheduleDate:String? = null,
        var dueDate:String? = null,
        var idBoleto:Long? =null):Serializable