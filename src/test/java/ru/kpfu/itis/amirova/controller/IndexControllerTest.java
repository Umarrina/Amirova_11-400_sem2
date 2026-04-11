package ru.kpfu.itis.amirova.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.kpfu.itis.amirova.config.SecurityConfig;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(IndexController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void index_ReturnsIndexView() throws Exception {
        mockMvc.perform(get("/index")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
}