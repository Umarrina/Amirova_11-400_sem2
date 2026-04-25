package ru.kpfu.itis.amirova.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.amirova.aop.Benchmark;
import ru.kpfu.itis.amirova.aop.Metrics;
import ru.kpfu.itis.amirova.model.Note;
import ru.kpfu.itis.amirova.repository.NoteRepository;
import ru.kpfu.itis.amirova.service.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteController {
    private final NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping
    public String myNotes(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Note> notes = noteRepository.findByAuthor(userDetails.getUser());
        model.addAttribute("notes", notes);
        return "notes";
    }

    @GetMapping("/public")
    public String publicNotes(Model model) {
        model.addAttribute("notes", noteRepository.findByIsPublicTrue());
        return "public_notes";
    }

    @GetMapping("/create")
    @Metrics
    @Benchmark
    public String createNote(Model model) {
        model.addAttribute("note", new Note());
        return "note_form";
    }

    @PostMapping("/create")
    @Metrics
    @Benchmark
    public String createNote(@ModelAttribute Note note,
                             @RequestParam(value = "isPublic", required = false) Boolean isPublic,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        note.setAuthor(userDetails.getUser());
        note.setCreatedAt(LocalDateTime.now());
        note.setIsPublic(isPublic != null && isPublic); // если null -> false
        noteRepository.save(note);
        return "redirect:/notes";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заметка не найдена"));
        if (!note.getAuthor().getId().equals(userDetails.getUser().getId())) {
            return "redirect:/notes?error=not_your_note";
        }
        model.addAttribute("note", note);
        return "note_form";
    }

    @PostMapping("/{id}/edit")
    public String updateNote(@PathVariable("id") Long id,
                             @ModelAttribute Note updatedNote,
                             @RequestParam(value = "isPublic", required = false) Boolean isPublic,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заметка не найдена"));
        if (!note.getAuthor().getId().equals(userDetails.getUser().getId())) {
            return "redirect:/notes?error=not_your_note";
        }
        note.setTitle(updatedNote.getTitle());
        note.setContent(updatedNote.getContent());
        note.setIsPublic(isPublic != null && isPublic);
        noteRepository.save(note);
        return "redirect:/notes";
    }

    @PostMapping("/{id}/delete")
    public String deleteNote(@PathVariable("id") Long id,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заметка не найдена"));
        if (!note.getAuthor().getId().equals(userDetails.getUser().getId())) {
            return "redirect:/notes?error=not_your_note";
        }
        noteRepository.delete(note);
        return "redirect:/notes";
    }
}
