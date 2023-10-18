package br.com.bjbraz.entity.auth

import lombok.Data
import lombok.Getter
import lombok.Setter
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Getter
@Setter
@Data
@Document(value = "auth")
class AuthEntity (

    @Id
    var _id: String?=null,

    var token: String?=null,

    var expiresIn: String?=null,

    var jti: String?=null,

    var scope: String?=null,

    var tokenType: String?=null,

    var dtRef: String?=null

){
}