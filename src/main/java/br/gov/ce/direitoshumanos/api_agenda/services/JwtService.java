package br.gov.ce.direitoshumanos.api_agenda.services;

import br.gov.ce.direitoshumanos.api_agenda.models.UsuarioDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    private final long EXPIRATION = 86400000; // 1 dia
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(UsuarioDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("nome", user.getNome())
                .claim("id", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    public String extractCpf(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isTokenValid(String token, UsuarioDetails user) {
        return extractCpf(token).equals(user.getUsername());
    }
}
