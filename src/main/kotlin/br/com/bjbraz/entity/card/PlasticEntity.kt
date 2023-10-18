package br.com.bjbraz.entity.card

import lombok.Builder
import lombok.Data
import org.springframework.data.annotation.Id

@Data
@Builder
class PlasticEntity (

        @Id
        val id:Long? = null,

        val holder:String?=null,

        val message:String?=null,

        val image:String?=null,

        val imageId:Int=1
)