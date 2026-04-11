package ru.kpfu.itis.amirova.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.kpfu.itis.amirova.config.SecurityConfig;
import ru.kpfu.itis.amirova.model.Note;
import ru.kpfu.itis.amirova.model.Role;
import ru.kpfu.itis.amirova.model.User;
import ru.kpfu.itis.amirova.repository.NoteRepository;
import ru.kpfu.itis.amirova.service.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NoteRepository noteRepository;

    private User createUser(Long id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword("pass");
        user.setEmail("user@example.com");
        user.setEnabled(true);
        Role role = new Role();
        role.setName("ROLE_USER");
        user.setRoles(List.of(role));
        return user;
    }

    private CustomUserDetails customUserDetails(User user) {
        return new CustomUserDetails(user);
    }

    @Test
    void myNotes_ShouldReturnNotesView() throws Exception {
        User owner = createUser(1L, "owner");
        Note note = new Note();
        note.setId(1L);
        note.setTitle("My Title");
        note.setContent("Content");
        note.setCreatedAt(LocalDateTime.now());
        note.setIsPublic(true);
        given(noteRepository.findByAuthor(owner)).willReturn(List.of(note));

        mockMvc.perform(get("/notes").with(user(customUserDetails(owner))))
                .andExpect(status().isOk())
                .andExpect(view().name("notes"));
    }

    @Test
    void publicNotes_ShouldReturnPublicNotesView() throws Exception {
        User author = createUser(2L, "author");
        Note publicNote = new Note();
        publicNote.setId(2L);
        publicNote.setTitle("Public Title");
        publicNote.setContent("Content");
        publicNote.setCreatedAt(LocalDateTime.now());
        publicNote.setIsPublic(true);
        publicNote.setAuthor(author);
        given(noteRepository.findByIsPublicTrue()).willReturn(List.of(publicNote));

        mockMvc.perform(get("/notes/public"))
                .andExpect(status().isOk())
                .andExpect(view().name("public_notes"));
    }

    @Test
    void createNote_ShouldRedirect() throws Exception {
        User user = createUser(1L, "user");
        mockMvc.perform(post("/notes/create")
                        .param("title", "New note")
                        .param("content", "Text")
                        .param("isPublic", "true")
                        .with(user(customUserDetails(user)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));
        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void editForm_WhenOwner_ReturnsForm() throws Exception {
        User owner = createUser(1L, "owner");
        Note note = new Note();
        note.setId(5L);
        note.setAuthor(owner);
        given(noteRepository.findById(5L)).willReturn(Optional.of(note));

        mockMvc.perform(get("/notes/5/edit").with(user(customUserDetails(owner))))
                .andExpect(status().isOk())
                .andExpect(view().name("note_form"));
    }

    @Test
    void deleteNote_WhenOwner_Deletes() throws Exception {
        User owner = createUser(1L, "owner");
        Note note = new Note();
        note.setId(5L);
        note.setAuthor(owner);
        given(noteRepository.findById(5L)).willReturn(Optional.of(note));

        mockMvc.perform(post("/notes/5/delete")
                        .with(user(customUserDetails(owner)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));
        verify(noteRepository).delete(note);
    }
}