package ru.kpfu.itis.amirova.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.amirova.aop.Benchmark;
import ru.kpfu.itis.amirova.aop.Metrics;
import ru.kpfu.itis.amirova.config.MailProperties;
import ru.kpfu.itis.amirova.dto.CreateUserDto;
import ru.kpfu.itis.amirova.dto.UserDto;
import ru.kpfu.itis.amirova.model.Role;
import ru.kpfu.itis.amirova.model.User;
import ru.kpfu.itis.amirova.repository.RoleRepository;
import ru.kpfu.itis.amirova.repository.UserRepository;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JavaMailSender mailSender,
                           MailProperties mailProperties) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
    }

    @Override
    @Transactional
    @Metrics
    @Benchmark
    public void createUser(CreateUserDto createUserDto) {
        if (userRepository.findByUsername(createUserDto.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(createUserDto.username());
        user.setEmail(createUserDto.email());
        user.setPassword(passwordEncoder.encode(createUserDto.password()));
        user.setEnabled(false);
        user.setVerificationCode(UUID.randomUUID().toString());

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
        user.setRoles(List.of(userRole));

        userRepository.save(user);
        sendVerificationMail(user);

        System.out.println("Verification code for " + user.getUsername() + ": " + user.getVerificationCode());
    }

    private void sendVerificationMail(User user) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(mailProperties.from(), mailProperties.sender());
            helper.setTo(user.getEmail());
            helper.setSubject(mailProperties.subject());

            String content = mailProperties.content()
                    .replace("$name", user.getUsername())
                    .replace("$url", mailProperties.baseUrl() + "/verification?code=" + user.getVerificationCode());

            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserDto convertToDto(User user) {
        return new UserDto(user.getId(), user.getUsername());
    }
}