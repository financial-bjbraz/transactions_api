package br.com.bjbraz.entity.card

import lombok.Builder
import lombok.Data
import org.springframework.data.annotation.Id

@Data
@Builder
class EmbossingEntity (

        @Id
        val id:Long? = null,

        val plastic: PlasticEntity?=null,

        val letter: LetterEntity?=null

)