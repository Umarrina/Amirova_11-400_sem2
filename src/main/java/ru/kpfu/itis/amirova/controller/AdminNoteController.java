package ru.kpfu.itis.amirova.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.amirova.aop.Benchmark;
import ru.kpfu.itis.amirova.aop.Metrics;
import ru.kpfu.itis.amirova.dto.NoteDto;
import ru.kpfu.itis.amirova.model.Note;
import ru.kpfu.itis.amirova.repository.NoteRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminNoteController {

    private final NoteRepository noteRepository;

    public AdminNoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping("/notes")
    public List<NoteDto> getAllNotes() {
        return noteRepository.findAllWithAuthor().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/notes/{id}")
    @Metrics
    @Benchmark
    public void deleteNote(@PathVariable Long id) {
        noteRepository.deleteNoteById(id);
    }

    private NoteDto toDto(Note note) {
        return new NoteDto(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt(),
                note.getIsPublic(),
                note.getAuthor().getUsername()
        );
    }
}
