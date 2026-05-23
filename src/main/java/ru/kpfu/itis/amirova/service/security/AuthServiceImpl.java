package ru.kpfu.itis.amirova.service.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kpfu.itis.amirova.dto.security.JwtRefreshRequest;
import ru.kpfu.itis.amirova.dto.security.JwtRequest;
import ru.kpfu.itis.amirova.dto.security.JwtResponse;
import ru.kpfu.itis.amirova.filter.JwtProvider;
import ru.kpfu.itis.amirova.model.User;
import ru.kpfu.itis.amirova.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    public AuthServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public JwtResponse login(JwtRequest jwtRequest) {
        User user = userRepository.findByUsername(jwtRequest.login())
                .orElseThrow(() -> new UsernameNotFoundException(jwtRequest.login()));

        if (!bCryptPasswordEncoder.matches(jwtRequest.password(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(refreshToken, user.getUsername());
        return new JwtResponse(accessToken, refreshToken);
    }

    @Override
    public JwtResponse refreshToken(JwtRefreshRequest jwtRefreshRequest) {
        return null;
    }

    @Override
    public JwtResponse token(JwtRefreshRequest jwtRefreshRequest) {
        return null;
    }
}
