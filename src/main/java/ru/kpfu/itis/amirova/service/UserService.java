package ru.kpfu.itis.amirova.service;

import ru.kpfu.itis.amirova.dto.CreateUserDto;
import ru.kpfu.itis.amirova.dto.UserDto;

import java.util.List;

public interface UserService {
    void createUser(CreateUserDto createUserDto);
    List<UserDto> findAll();
}