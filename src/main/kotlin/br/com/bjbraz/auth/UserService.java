package br.com.bjbraz.auth;


import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    // this is just an example, you can load the user from the database from the repository

    private Map<String, User> data;

    @PostConstruct
    public void init(){
        data = new HashMap<>();

        //username:passwowrd -> user:user
        data.put("usuario", new User("usuario", "cBrlgyL2GI2GINuLUUwgojITuIufFycpLG4490dhGtY=","49650416000144", "MAIN:123456", "ACCOUNT:1010101010101", true, Arrays.asList(Role.ROLE_USER)));

        //username:passwowrd -> admin:admin
        data.put("admin", new User("admin", "dQNjUIMorJb8Ubj2+wVGYp6eAeYkdekqAcnYp+aRq5w=","49650416000144","main_account2", "account_id2", true, Arrays.asList(Role.ROLE_ADMIN)));
    }

    public Mono<User> findByUsername(String username) {
        if (data.containsKey(username)) {
            return Mono.just(data.get(username));
        } else {
            return Mono.empty();
        }
    }

}