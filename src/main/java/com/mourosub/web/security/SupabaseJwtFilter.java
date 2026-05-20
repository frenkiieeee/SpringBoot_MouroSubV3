package com.mourosub.web.security;

import com.mourosub.web.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Protege el panel admin (/admin/**).
 *
 * Lee el token de sesion de Supabase desde la cookie "sb_access_token",
 * verifica su firma con el secreto compartido (JWT_SECRET) y comprueba que
 * el usuario este marcado como admin en la tabla usuarios. Si algo falla
 * redirige al login. No hace falta llamar a Supabase: con el secreto basta
 * para validar el token localmente.
 */
@Component
public class SupabaseJwtFilter extends OncePerRequestFilter {

    private static final String COOKIE = "sb_access_token";

    private final UsuarioRepository usuarioRepository;
    private final SecretKey key;

    public SupabaseJwtFilter(UsuarioRepository usuarioRepository,
                             @Value("${JWT_SECRET}") String jwtSecret) {
        this.usuarioRepository = usuarioRepository;
        // Supabase firma con HS256 usando el secreto como bytes UTF-8.
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Solo protegemos el panel admin; el resto de la web pasa sin tocar.
        if (!request.getRequestURI().startsWith("/admin")) {
            chain.doFilter(request, response);
            return;
        }

        if (esAdmin(leerCookie(request))) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect("/login");
        }
    }

    // Verifica la firma y caducidad del JWT y que el usuario sea admin.
    private boolean esAdmin(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            UUID supabaseUserId = UUID.fromString(claims.getSubject());
            return usuarioRepository.findBySupabaseUserId(supabaseUserId)
                    .map(u -> u.isAdmin())
                    .orElse(false);
        } catch (Exception ex) {
            // Firma invalida, token caducado, sub no es un UUID, etc.
            return false;
        }
    }

    private String leerCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
