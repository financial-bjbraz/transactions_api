package br.com.bjbraz.auth;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Log4j2
public class AuthenticationREST {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PBKDF2Encoder passwordEncoder;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @CrossOrigin(origins = "http://192.168.0.18:4200")
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest ar) {
        return userService.findByUsername(ar.getUsername()).map((userDetails) -> {
            log.info("Authenticate :: {} , {}", userDetails.getUsername(), userDetails.getAccountId());
            if (passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword())) {
                return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails)));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

}
