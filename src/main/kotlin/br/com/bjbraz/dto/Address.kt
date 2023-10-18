package br.com.bjbraz.dto


class Address (
        val street:String?=null,
        val number:String?=null,
        val district: String?=null,
        val city:String?=null,
        val state:String?=null,
        var zipcode:String?=null
)


/*    "address": {
"street": "Rua dos Testes",
"number": "123",
"district": "Tamboré",
"zipcode": "06460080",
"city": "Barueri",
"state": "SP"
},*/