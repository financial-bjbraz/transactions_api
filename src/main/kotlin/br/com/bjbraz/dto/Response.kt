package br.com.bjbraz.dto

import com.fasterxml.jackson.annotation.JsonInclude
import lombok.*

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Response<T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private val data: T? = null
    private val code = 0
    private val message: String? = null
}
