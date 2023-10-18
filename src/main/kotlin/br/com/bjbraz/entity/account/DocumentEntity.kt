package br.com.bjbraz.entity.account

import lombok.AllArgsConstructor
import lombok.EqualsAndHashCode
import lombok.Getter
import lombok.Setter
import org.springframework.data.annotation.Id

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
class DocumentEntity(
    @Id
    val idDocument: Long? = null,

    val document: String? = null,

    val type: String = "CPF"
)

/*
{
        "document": "51005633037",
        "type": "CPF"
      }
 */