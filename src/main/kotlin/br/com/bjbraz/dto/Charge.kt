package br.com.bjbraz.dto

class Charge (var transferCode:String?="", var amount:Double?=null, var summary:String="Charge", val fok:String = "", var requestId:String?=null, var externalCode:String = "") {
}