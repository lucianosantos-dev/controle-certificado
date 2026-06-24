package com.lucianodev.controlecertificado.config;

import com.lucianodev.controlecertificado.entities.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

    @Value("${JWT_SECRET}")
    private String secret;

    private static final String ISSUER = "api-solicitacao-certificado";

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Usuario usuario) {
        try {
            return Jwts.builder()
                    .issuer(ISSUER)
                    .subject(usuario.getUsername())
                    .claim("id", usuario.getId())
                    .claim("perfil", usuario.getPerfil().name())
                    .claim("nome", usuario.getNome())
                    .expiration(generateExpiration())
                    .signWith(getSecretKey())
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar TOKEN", e);
        }
    }

    public String validateToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            throw new RuntimeException("TOKEN inválido ou expirado", e);
        }
    }

    private Date generateExpiration() {
        Instant instant = LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
        return Date.from(instant);
    }
}
