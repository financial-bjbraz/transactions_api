package br.com.bjbraz.entity.account

import lombok.Builder
import lombok.Data
import org.springframework.data.annotation.Id

@Data
@Builder
class AddressEntity (
        @Id
        val idAddress: Long?=null,

        val street:String?=null,

        val number:String?=null,

        val district: String?=null,

        val city:String?=null,

        val state:String?=null,

        val zipCode:String?=null
)