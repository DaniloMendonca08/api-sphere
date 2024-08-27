package br.com.danilo.apisphere.auth;

import br.com.danilo.apisphere.user.User;
import br.com.danilo.apisphere.user.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private final UserRepository userRepository;
    Algorithm algorithm = Algorithm.HMAC256("assinatura");

    public TokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Token create(User user) {

        //variavel contendo o tempo que irá expirar e convertendo para o horário brasileiro
        var expiresAt = LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.ofHours(-3));
        String token = JWT.create()
                .withIssuer("sphere")
                .withSubject(user.getEmail())
                .withClaim("role", "admin")
                .withExpiresAt(expiresAt)
                .sign(algorithm);

        return new Token(token);
    }

    public User getUserFromToken(String token) {
        var email = JWT.require(algorithm)
                .withIssuer("sphere")
                .build()
                .verify(token)
                .getSubject();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
