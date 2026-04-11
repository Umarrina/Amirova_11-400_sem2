package ru.kpfu.itis.amirova.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kpfu.itis.amirova.model.User;
import ru.kpfu.itis.amirova.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private CustomUserDetailsService service;

    @Test
    void loadUserByUsername_Success() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        user.setEnabled(true);
        given(userRepository.findByUsernameWithRoles("user")).willReturn(Optional.of(user));

        var details = service.loadUserByUsername("user");
        assertThat(details.getUsername()).isEqualTo("user");
        assertThat(details.isEnabled()).isTrue();
    }

    @Test
    void loadUserByUsername_NotFound_Throws() {
        given(userRepository.findByUsernameWithRoles("unknown")).willReturn(Optional.empty());
        assertThatThrownBy(() -> service.loadUserByUsername("unknown"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}