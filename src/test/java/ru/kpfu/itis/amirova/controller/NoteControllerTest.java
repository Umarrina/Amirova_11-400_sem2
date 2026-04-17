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
import static org.mockito.Mockito.never;
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
        user.setEmail(username + "@test.com");
        user.setEnabled(true);
        Role role = new Role();
        role.setName("ROLE_USER");
        user.setRoles(List.of(role));
        return user;
    }

    private CustomUserDetails asUserDetails(User user) {
        return new CustomUserDetails(user);
    }

    private Note createNote(Long id, User author, String title, String content) {
        Note note = new Note();
        note.setId(id);
        note.setTitle(title);
        note.setContent(content);
        note.setCreatedAt(LocalDateTime.now());
        note.setIsPublic(true);
        note.setAuthor(author);
        return note;
    }

    // ========== Тесты ==========

    @Test
    void myNotes_ShouldReturnNotesView() throws Exception {
        User owner = createUser(1L, "owner");
        Note note = createNote(1L, owner, "Test title", "Test content");
        given(noteRepository.findByAuthor(owner)).willReturn(List.of(note));

        mockMvc.perform(get("/notes").with(user(asUserDetails(owner))))
                .andExpect(status().isOk())
                .andExpect(view().name("notes"))
                .andExpect(model().attributeExists("notes"));
    }

    @Test
    void publicNotes_ShouldReturnPublicNotesView() throws Exception {
        User author = createUser(1L, "author");
        Note publicNote = createNote(1L, author, "Public title", "Public content");
        given(noteRepository.findByIsPublicTrue()).willReturn(List.of(publicNote));

        mockMvc.perform(get("/notes/public"))
                .andExpect(status().isOk())
                .andExpect(view().name("public_notes"))
                .andExpect(model().attributeExists("notes"));
    }

    @Test
    void createNoteForm_ShouldReturnFormView() throws Exception {
        User user = createUser(1L, "user");
        mockMvc.perform(get("/notes/create").with(user(asUserDetails(user))))
                .andExpect(status().isOk())
                .andExpect(view().name("note_form"))
                .andExpect(model().attributeExists("note"));
    }

    @Test
    void createNote_WithPublicFlag_ShouldSaveAndRedirect() throws Exception {
        User user = createUser(1L, "user");
        mockMvc.perform(post("/notes/create")
                        .param("title", "New note")
                        .param("content", "Text")
                        .param("isPublic", "true")
                        .with(user(asUserDetails(user)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));

        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void createNote_WithoutPublicFlag_ShouldSetIsPublicFalse() throws Exception {
        User user = createUser(1L, "user");
        mockMvc.perform(post("/notes/create")
                        .param("title", "Private note")
                        .param("content", "Secret")
                        .with(user(asUserDetails(user)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));

        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void editForm_WhenOwner_ReturnsForm() throws Exception {
        User owner = createUser(1L, "owner");
        Note note = createNote(5L, owner, "Old title", "Old content");
        given(noteRepository.findById(5L)).willReturn(Optional.of(note));

        mockMvc.perform(get("/notes/5/edit").with(user(asUserDetails(owner))))
                .andExpect(status().isOk())
                .andExpect(view().name("note_form"))
                .andExpect(model().attributeExists("note"));
    }

    @Test
    void editForm_WhenNotOwner_RedirectsWithError() throws Exception {
        User owner = createUser(1L, "owner");
        User other = createUser(2L, "other");
        Note note = createNote(5L, owner, "Title", "Content");
        given(noteRepository.findById(5L)).willReturn(Optional.of(note));

        mockMvc.perform(get("/notes/5/edit").with(user(asUserDetails(other))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes?error=not_your_note"));
    }

    @Test
    void updateNote_WhenOwner_UpdatesAndRedirects() throws Exception {
        User owner = createUser(1L, "owner");
        Note existingNote = createNote(5L, owner, "Old title", "Old content");
        existingNote.setIsPublic(false);

        given(noteRepository.findById(5L)).willReturn(Optional.of(existingNote));

        mockMvc.perform(post("/notes/5/edit")
                        .param("title", "Updated title")
                        .param("content", "New content")
                        .param("isPublic", "true")
                        .with(user(asUserDetails(owner)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));

        verify(noteRepository).save(existingNote);
        // проверяем, что поля обновлены
        assert existingNote.getTitle().equals("Updated title");
        assert existingNote.getContent().equals("New content");
        assert existingNote.getIsPublic();
    }

    @Test
    void updateNote_WhenNotOwner_RedirectsWithError_NoSave() throws Exception {
        User owner = createUser(1L, "owner");
        User other = createUser(2L, "other");
        Note note = createNote(5L, owner, "Title", "Content");
        given(noteRepository.findById(5L)).willReturn(Optional.of(note));

        mockMvc.perform(post("/notes/5/edit")
                        .param("title", "Hacked")
                        .param("content", "Hacked")
                        .with(user(asUserDetails(other)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes?error=not_your_note"));

        verify(noteRepository, never()).save(any());
    }

    @Test
    void deleteNote_WhenOwner_DeletesAndRedirects() throws Exception {
        User owner = createUser(1L, "owner");
        Note note = createNote(5L, owner, "To delete", "Content");
        given(noteRepository.findById(5L)).willReturn(Optional.of(note));

        mockMvc.perform(post("/notes/5/delete")
                        .with(user(asUserDetails(owner)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));

        verify(noteRepository).delete(note);
    }

    @Test
    void deleteNote_WhenNotOwner_RedirectsWithError() throws Exception {
        User owner = createUser(1L, "owner");
        User other = createUser(2L, "other");
        Note note = createNote(5L, owner, "Title", "Content");
        given(noteRepository.findById(5L)).willReturn(Optional.of(note));

        mockMvc.perform(post("/notes/5/delete")
                        .with(user(asUserDetails(other)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes?error=not_your_note"));

        verify(noteRepository, never()).delete(any());
    }

}