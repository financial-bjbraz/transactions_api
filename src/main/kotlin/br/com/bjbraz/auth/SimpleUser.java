package br.com.bjbraz.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@ToString
@NoArgsConstructor
public class SimpleUser implements Serializable {

    private String username;
    private String cpf;
    private String mainAccountId;
    private String accountId;

    public SimpleUser(String username, String cpf, String mainAccountId, String accountId) {
        this.username = username;
        this.cpf = cpf;
        this.mainAccountId = mainAccountId;
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) { this.cpf = cpf;}

    public String getMainAccountId() {
        return mainAccountId;
    }

    public void setMainAccountId(String mainAccountId) { this.accountId = accountId;}

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) { this.accountId = accountId;}
}
