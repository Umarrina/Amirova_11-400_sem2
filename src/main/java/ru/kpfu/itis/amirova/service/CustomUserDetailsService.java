package ru.kpfu.itis.amirova.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.amirova.model.User;
import ru.kpfu.itis.amirova.repository.UserRepository;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsernameWithRoles(username);
        return user.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}