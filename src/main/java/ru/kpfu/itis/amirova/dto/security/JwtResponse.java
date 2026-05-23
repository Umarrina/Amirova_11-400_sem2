package ru.kpfu.itis.amirova.dto.security;

public record JwtResponse (
        String accessToken,
        String refreshToken
) {
}
