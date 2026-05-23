package ru.kpfu.itis.amirova.service.security;

import ru.kpfu.itis.amirova.dto.security.JwtRefreshRequest;
import ru.kpfu.itis.amirova.dto.security.JwtRequest;
import ru.kpfu.itis.amirova.dto.security.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest jwtRequest);
    JwtResponse refreshToken(JwtRefreshRequest jwtRefreshRequest);
    JwtResponse token(JwtRefreshRequest jwtRefreshRequest);
}
