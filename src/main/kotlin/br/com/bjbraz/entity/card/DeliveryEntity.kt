package br.com.bjbraz.entity.card

import lombok.Builder
import lombok.Data
import org.springframework.data.annotation.Id

@Data
@Builder
class DeliveryEntity (

        @Id
        val id:Long? = null,

        val recipient: RecipientEntity?=null
)