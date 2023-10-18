package br.com.bjbraz.auth;


import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author abraz
 */
@Log4j2
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        log.info("Calling authenticate %s ", authentication);
        try {
            String username      = jwtUtil.getUsernameFromToken(authToken);
            String mainAccountId = jwtUtil.getHeaderParam(authToken, "mainAccountId");
            String accountId = jwtUtil.getHeaderParam(authToken, "accountId");
            String cpf = jwtUtil.getHeaderParam(authToken, "cpf");

            if (!jwtUtil.validateToken(authToken)) {
                return Mono.empty();
            }
            Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
            List<String> rolesMap = claims.get("role", List.class);
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String rolemap : rolesMap) {
                authorities.add(new SimpleGrantedAuthority(rolemap));

                //new User(username,  cpf, mainAccountId, accountId)
            }
            log.info("Calling authenticate:: auth ok  %s %s ", username, cpf);
            return Mono.just(new UsernamePasswordAuthenticationToken(username, new SimpleUser(username,  cpf, mainAccountId, accountId), authorities));
        } catch (Exception e) {
            return Mono.empty();
        }
    }
}
