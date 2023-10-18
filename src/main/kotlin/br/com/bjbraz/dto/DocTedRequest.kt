package br.com.bjbraz.dto

import lombok.Data

@Data
class DocTedRequest (
        var financialOperationKey:String?=null,
        var externalId:String?=null,
        var amount:Double?=null,
        var type:String?=null, //DOC-TED
        var summary:String?=null,
        var scheduleDate:String?=null,//YYYY-mm-dd
        var bank:DocTedBankBeneficiary? = null

)