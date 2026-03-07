package ru.kpfu.itis.amirova.service;

import org.springframework.stereotype.Service;
import ru.kpfu.itis.amirova.dto.UserDto;
import ru.kpfu.itis.amirova.model.User;
import ru.kpfu.itis.amirova.repository.UserRepository;
import ru.kpfu.itis.amirova.repository.UserRepositoryHibernate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepositoryHibernate userRepositoryHibernate;
    private final UserRepository userRepository;

    public UserService(UserRepositoryHibernate userRepositoryHibernate, UserRepository userRepository) {
        this.userRepositoryHibernate = userRepositoryHibernate;
        this.userRepository = userRepository;
    }

    public List<UserDto> findAll() {

        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

//        return userRepositoryHiber.findAll().stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
    }

    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDto)
                .orElse(null);
    }

    public void create(User user) {
        userRepository.create(user);
    }

    public void update(User user) {
        userRepository.update(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    private UserDto convertToDto(User user) {
        return new UserDto(user.getId(), user.getUsername());
    }
}