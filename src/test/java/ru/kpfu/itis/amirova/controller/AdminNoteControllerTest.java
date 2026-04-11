package ru.kpfu.itis.amirova.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.kpfu.itis.amirova.config.SecurityConfig;
import ru.kpfu.itis.amirova.model.Note;
import ru.kpfu.itis.amirova.model.User;
import ru.kpfu.itis.amirova.repository.NoteRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminNoteController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class AdminNoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NoteRepository noteRepository;

    @Test
    void getAllNotes_AsAdmin_ReturnsList() throws Exception {
        User author = new User();
        author.setUsername("admin");
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Secret");
        note.setContent("Content");
        note.setCreatedAt(LocalDateTime.now());
        note.setIsPublic(true);
        note.setAuthor(author);

        given(noteRepository.findAllWithAuthor()).willReturn(List.of(note));

        mockMvc.perform(get("/admin/notes")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Secret"))
                .andExpect(jsonPath("$[0].authorUsername").value("admin"));
    }

    @Test
    void deleteNote_AsAdmin_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/admin/notes/10")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(noteRepository).deleteNoteById(10L);
    }

    @Test
    void getAllNotes_WithoutAdminRole_ShouldForbid() throws Exception {
        mockMvc.perform(get("/admin/notes")
                        .with(user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }
}