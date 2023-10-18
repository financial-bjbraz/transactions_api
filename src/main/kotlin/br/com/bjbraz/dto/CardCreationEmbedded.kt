package br.com.bjbraz.dto

import lombok.Data

@Data
class CardCreationEmbedded(
        val errors:List<CallBackError>?=null
)

//        "_embedded":{"errors":[{"logref":"REQUIRED","message":"'Address. Number' should not be empty.","path":"/accounts[0]/cards[0]/delivery/content/recipient/address/number"}]}}