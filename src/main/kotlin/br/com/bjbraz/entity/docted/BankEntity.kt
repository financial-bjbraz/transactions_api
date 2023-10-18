package br.com.bjbraz.entity.docted

import lombok.Builder
import lombok.Data
import org.springframework.beans.factory.annotation.Required
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotEmpty

@Data
@Builder
@Document(collection = "bank")
class BankEntity (

        @NotEmpty
        var name:String? = null,

        @NotEmpty
        @Id
        val number:Long? = null

)