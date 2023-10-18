package br.com.bjbraz.entity.card

import lombok.ToString
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@ToString
@Document(value = "transactions")
data class LetterEntity (

        @Id
        val id:String? = null,

        val color:String?=null,

        val title:String="",

        val message:String="",

        val signature:String=""
)