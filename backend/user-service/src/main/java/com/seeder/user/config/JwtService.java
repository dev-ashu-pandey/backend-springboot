package com.seeder.user.config;

import com.seeder.user.entity.User;
import com.seeder.user.exception.InvalidUserCredentials;
import com.seeder.user.repository.IUserRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    @Value("${SECRET_ID}")
    private String SECRET;

    private final IUserRepository userRepository;
    private final BCryptPasswordEncoder bcryptPassword;

    public String generateToken(String email, String password) {
        log.info("Inside generate token");
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isEmpty()) {
                throw new EntityNotFoundException("User not found with email: " + email);
            }
            if (!bcryptPassword.matches(password, user.get().getPassword())) {
                throw new InvalidUserCredentials("Invalid credentials");
            }
            return createToken(email);
    }


    public String createToken(String userName) {
        return Jwts.builder().setSubject(userName).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)).signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }


    public Key getSignKey() {
        byte[] keyByte = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyByte);
    }
}