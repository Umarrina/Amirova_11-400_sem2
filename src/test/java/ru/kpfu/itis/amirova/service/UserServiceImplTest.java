package ru.kpfu.itis.amirova.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kpfu.itis.amirova.config.MailProperties;
import ru.kpfu.itis.amirova.dto.CreateUserDto;
import ru.kpfu.itis.amirova.dto.UserDto;
import ru.kpfu.itis.amirova.model.Role;
import ru.kpfu.itis.amirova.model.User;
import ru.kpfu.itis.amirova.repository.RoleRepository;
import ru.kpfu.itis.amirova.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MailProperties mailProperties;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_Success() {
        CreateUserDto dto = new CreateUserDto("new", "raw", "e@x.com");
        given(userRepository.findByUsername("new")).willReturn(Optional.empty());
        given(passwordEncoder.encode("raw")).willReturn("encoded");
        Role role = new Role();
        role.setName("ROLE_USER");
        given(roleRepository.findByName("ROLE_USER")).willReturn(Optional.of(role));
        given(mailSender.createMimeMessage()).willReturn(mimeMessage);
        given(mailProperties.from()).willReturn("from");
        given(mailProperties.sender()).willReturn("sender");
        given(mailProperties.subject()).willReturn("subj");
        given(mailProperties.content()).willReturn("$name $url");
        given(mailProperties.baseUrl()).willReturn("http://localhost");

        userService.createUser(dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.getUsername()).isEqualTo("new");
        assertThat(saved.getPassword()).isEqualTo("encoded");
        assertThat(saved.isEnabled()).isFalse();
        assertThat(saved.getVerificationCode()).isNotNull();
    }

    @Test
    void createUser_UsernameExists_Throws() {
        given(userRepository.findByUsername("exists")).willReturn(Optional.of(new User()));
        assertThatThrownBy(() -> userService.createUser(new CreateUserDto("exists", "p", "e@x.com")))
                .isInstanceOf(RuntimeException.class);
        verify(userRepository, never()).save(any());
    }

    @Test
    void findAll_ReturnsList() {
        User u = new User();
        u.setId(1L);
        u.setUsername("john");
        given(userRepository.findAll()).willReturn(List.of(u));
        List<UserDto> result = userService.findAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("john");
    }
}