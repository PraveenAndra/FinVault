package web.app.finvault.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

public interface JwtService {

     String generateToken(String username);
     SecretKey generateKey();
     Claims extractClaims(String token);
     String extractSubject(String token);
     boolean validateToken(String token);
     Date extractExpiration(String token);
}
