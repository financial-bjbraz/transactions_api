package br.com.bjbraz.dto

import lombok.Data

@Data
class Reissue (var idempotencyKey:String?="", var cpf:String?=null, var externalCode:String?=null)