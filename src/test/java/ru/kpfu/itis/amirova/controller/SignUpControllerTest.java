package ru.kpfu.itis.amirova.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.kpfu.itis.amirova.config.SecurityConfig;
import ru.kpfu.itis.amirova.dto.CreateUserDto;
import ru.kpfu.itis.amirova.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SignUpController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class SignUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void showSignUpForm_WithoutError_ReturnsView() throws Exception {
        mockMvc.perform(get("/sign_up"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign_up"));
    }

    @Test
    void register_Success_Redirects() throws Exception {
        mockMvc.perform(post("/sign_up")
                        .param("username", "john")
                        .param("password", "pass")
                        .param("email", "john@ex.com")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));

        verify(userService).createUser(any(CreateUserDto.class));
    }

    @Test
    void register_Failure_RedirectsWithError() throws Exception {
        doThrow(new RuntimeException("Exists")).when(userService).createUser(any());

        mockMvc.perform(post("/sign_up")
                        .param("username", "existing")
                        .param("password", "pass")
                        .param("email", "e@x.com")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sign_up?error"));
    }
}