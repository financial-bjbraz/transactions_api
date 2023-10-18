package br.com.bjbraz.entity.card

enum class BoletoStatus(val nome:String) {
        CREATED("CREATED"),
        REQUESTED("REQUESTED"),
        CANCELLED("CANCELLED"),
        PAYED("PAYED")
}