package br.com.bjbraz.dto

import lombok.Data

@Data
class RequestReissue(val idempotencyKey:String, val externalCode:String, val reason:String, val holder: Holder, val gender:String, val delivery: Delivery, val embossing: Embossing) {

    /*
    {
        "externalCode": "affayxklaih2aeaadagsD2aa",
        "reason": "loss",
        "holder": {
            "name": "hubintech001",
            "document": "15141680223",
            "email": "hubintech001@mail.me",
            "homePhone": "1135242424",
            "mobilePhone": "11999992424",
            "birthDate": "1970-09-11",
            "address": {
                "street": "Luiz Scott",
                "number": "209",
                "complement": "",
                "district": "Jardim Iracema",
                "zipcode": "06440260",
                "city": "Barueri",
                "state": "SP"
            },
            "gender": "male"
        },
        "delivery": {
            "recipient": {
                "name": "principal",
                    "address": {
                        "street": "Luiz Scott",
                        "number": "209",
                        "complement": "",
                        "district": "Jardim Iracema",
                        "zipcode": "06440260",
                        "city": "Barueri",
                        "state": "SP"
                    }
            }
        },
        "embossing": {
            "plastic": {
                "holder": "hubintech001",
                "message": "Mensagem do cartao",
                "image": "blabla",
                "imageId": null
            },
            "letter": {
                "color": "blue",
                "title": "titulo",
                "message": "mensagem da carta berco",
                "signature": "Assinatura"
            }
        }
    }

    */

}