package br.com.bjbraz.entity.card

import br.com.bjbraz.entity.account.AddressEntity
import lombok.Builder
import lombok.Data
import org.springframework.data.annotation.Id

@Data
@Builder
class RecipientEntity (

        @Id
        val id:Long? = null,

        val name:String?=null,

        val address: AddressEntity?=null
)