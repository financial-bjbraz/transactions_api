package br.com.bjbraz.dto

class DocTedResponse (
        var externalId:String?=null,
        var amount:Double?=null,
        var status:String?=null, //DOC-TED
        var summary:String?=null,
        var scheduleDate:String?=null,//YYYY-mm-dd
        var transactionNumber:String? = null
)