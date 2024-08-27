package br.com.danilo.apisphere.auth;

import br.com.danilo.apisphere.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//para ele ser reconhecido como dependência
@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public AuthorizationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var header = request.getHeader("Authorization");
        if (header == null) {
            //passando para o próximo filtro caso o usuário não passe o header, ex: requisição POST para criar conta
            filterChain.doFilter(request, response);
            return;
        }
        //caso o header não comece com Bearer
        if (!header.startsWith("Bearer ")) {
            response.setStatus(401);
            response.addHeader("Content-Type", "application/json");
            response.getWriter().write("""
                {
                    "message": "Token must starts with Bearer"
                }
            """);
            return;
        }

        try {
            //pegando o token do header e removendo a parte do Bearer
            var token = header.replace("Bearer ", "");

            User user = tokenService.getUserFromToken(token);

            //autorizar
            var auth = new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    user.getPassword(),
                    List.of(new SimpleGrantedAuthority("USER"))
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(403);
            response.addHeader("Content-Type", "application/json");
            response.getWriter().write("""
                {
                    "message": "%se.getMessage()"
                }
            """.formatted(e.getMessage()));
            return;
        }
    }
}
