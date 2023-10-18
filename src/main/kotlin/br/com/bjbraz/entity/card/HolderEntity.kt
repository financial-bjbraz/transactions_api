package br.com.bjbraz.entity.card

import br.com.bjbraz.entity.account.AddressEntity
import lombok.Builder
import lombok.Data
import org.springframework.data.annotation.Id


@Data
@Builder
class HolderEntity (

        @Id
        val id:Long? = null,

        val name:String?=null,

        val document:String?=null,

        val email:String?=null,

        val mobilePhone:String?=null,

        val homePhone:String?=null,

        var birthDate: String?=null,

        val gender: String="male",

        val address: AddressEntity?=null
)