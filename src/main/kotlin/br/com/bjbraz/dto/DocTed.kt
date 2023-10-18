package br.com.bjbraz.dto

import br.com.bjbraz.entity.docted.DocTedType
import lombok.Data
import java.io.Serializable

@Data
class DocTed (
        var amount:Double?=null,
        var type:DocTedType?=null, //DOC-TED
        var summary:String?=null,
        var scheduleDate:String?=null,//YYYY-mm-dd
        var beneficiary:Long? = null,
        var cpf:String?=null,
        var externalId:String?=null
) : Serializable