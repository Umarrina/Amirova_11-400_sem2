package ru.kpfu.itis.amirova.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.amirova.model.ChatMessage;
import ru.kpfu.itis.amirova.repository.ChatMessageRepository;

import java.util.List;

@RestController
@RequestMapping("/admin/messages")
public class AdminChatController {

    @Autowired
    private ChatMessageRepository messageRepository;

    @GetMapping
    public List<ChatMessage> getAllMessages() {
        return messageRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}