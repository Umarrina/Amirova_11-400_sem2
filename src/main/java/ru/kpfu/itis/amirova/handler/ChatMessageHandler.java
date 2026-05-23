package ru.kpfu.itis.amirova.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import ru.kpfu.itis.amirova.dto.ChatMessageDto;
import ru.kpfu.itis.amirova.model.ChatMessage;
import ru.kpfu.itis.amirova.model.User;
import ru.kpfu.itis.amirova.repository.ChatMessageRepository;
import ru.kpfu.itis.amirova.service.CustomUserDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ChatMessageHandler {

    @Autowired
    private ChatMessageRepository messageRepository;

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public ChatMessageDto sendMessage(@Payload String content, SimpMessageHeaderAccessor headerAccessor) {
        Authentication auth = (Authentication) headerAccessor.getUser();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Not authenticated");
        }
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User author = userDetails.getUser();

        ChatMessage message = new ChatMessage();
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());
        message.setAuthor(author);
        ChatMessage saved = messageRepository.save(message);

        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(saved.getId());
        dto.setContent(saved.getContent());
        dto.setAuthorName(author.getUsername());
        dto.setSentAt(saved.getSentAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return dto;
    }
}