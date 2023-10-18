package br.com.bjbraz.dto

class CallBackError (
    val logref:String?=null,
    val message:String?=null,
    val path:String?=null

    ////        "_embedded":{"errors":[{"logref":"REQUIRED","message":"'Address. Number' should not be empty.","path":"/accounts[0]/cards[0]/delivery/content/recipient/address/number"}]}}
)