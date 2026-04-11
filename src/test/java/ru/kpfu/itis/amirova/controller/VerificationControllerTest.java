package ru.kpfu.itis.amirova.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.kpfu.itis.amirova.config.SecurityConfig;
import ru.kpfu.itis.amirova.model.User;
import ru.kpfu.itis.amirova.repository.UserRepository;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VerificationController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class VerificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void verifyByLink_ValidCode_Redirects() throws Exception {
        User user = new User();
        user.setEnabled(false);
        user.setVerificationCode("code");
        given(userRepository.findByVerificationCode("code")).willReturn(Optional.of(user));

        mockMvc.perform(get("/verification").param("code", "code"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?verified"));

        verify(userRepository).save(user);
    }

    @Test
    void verifyByPost_ValidCode_Redirects() throws Exception {
        User user = new User();
        user.setEnabled(false);
        user.setVerificationCode("postCode");
        given(userRepository.findByVerificationCode("postCode")).willReturn(Optional.of(user));

        mockMvc.perform(post("/verify")
                        .param("code", "postCode")
                        .with(csrf())
                        .with(user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?verified"));

        verify(userRepository).save(user);
    }

    @Test
    void verifyByPost_InvalidCode_ShowsError() throws Exception {
        given(userRepository.findByVerificationCode("wrong")).willReturn(Optional.empty());

        mockMvc.perform(post("/verify")
                        .param("code", "wrong")
                        .with(csrf())
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("verify"))
                .andExpect(model().attributeExists("error"));
    }
}