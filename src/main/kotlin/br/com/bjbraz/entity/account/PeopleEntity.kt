package br.com.bjbraz.entity.account

import com.fasterxml.jackson.annotation.JsonFormat
import lombok.*
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Data
@Builder
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Document(collection = "qrcode")
class PeopleEntity (
        @Id
        val id: Object?=null,

        val birthDate: Date?=null,

        val name:String?=null,

        val cardName: String?=null,

        val type: String = "PF",

        val gender: String = "male",

        val companyName: String = "BJBraz desenvolvimento de Sistemas Ltda",

        val email:String?=null,

        val homePhone:String?=null,

        val businessPhone:String?=null,

        val mobilePhone:String?=null,

        val address: AddressEntity?=null,

        val identifierDocument: IdentifierDocumentEntity?=null,

        val documents:List<DocumentEntity>?=null
)