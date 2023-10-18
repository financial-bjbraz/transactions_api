package br.com.bjbraz.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    private String username;
    private String password;
    private String cpf;
    private String mainAccountId;
    private String accountId;

    @Getter
    @Setter
    private Boolean enabled;

    @Getter @Setter
    private List<Role> roles;

    public User(String username) {
        this.username = username;
    }
    public User(String username, String cpf, String mainAccountId, String accountId) {
        this.username = username;
        this.cpf = cpf;
        this.mainAccountId = mainAccountId;
        this.accountId = accountId;
    }

    @Override
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList());
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }
    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public String getCpf() {
        return cpf;
    }

    @JsonProperty
    public void setCpf(String cpf) { this.cpf = cpf;}

    @JsonIgnore
    public String getMainAccountId() {
        return mainAccountId;
    }

    @JsonProperty
    public void setMainAccountId(String mainAccountId) { this.accountId = accountId;}

    @JsonIgnore
    public String getAccountId() {
        return accountId;
    }

    @JsonProperty
    public void setAccountId(String accountId) { this.accountId = accountId;}
}