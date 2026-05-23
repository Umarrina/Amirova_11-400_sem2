package ru.kpfu.itis.amirova.dto.security;

public record JwtRequest (
    String login,
    String password
) {}
