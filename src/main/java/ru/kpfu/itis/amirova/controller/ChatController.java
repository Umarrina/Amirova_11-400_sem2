package ru.kpfu.itis.amirova.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.amirova.dto.ChatMessageDto;
import ru.kpfu.itis.amirova.model.ChatMessage;
import ru.kpfu.itis.amirova.model.User;
import ru.kpfu.itis.amirova.repository.ChatMessageRepository;
import ru.kpfu.itis.amirova.service.CustomUserDetails;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatMessageRepository messageRepository;

    @GetMapping
    public String chatPage(Model model) {
        List<ChatMessage> messages = messageRepository.findTop50ByOrderBySentAtDesc();
        model.addAttribute("messages", messages);
        return "chat";
    }

    @GetMapping("/public")
    public String publicHistory(Model model) {
        List<ChatMessage> messages = messageRepository.findTop50ByOrderBySentAtDesc();
        model.addAttribute("messages", messages);
        return "public_chat";
    }

    @GetMapping("/my")
    public String myMessages(Authentication authentication, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        List<ChatMessage> messages = messageRepository.findByAuthor(user);
        model.addAttribute("messages", messages);
        return "my_messages";
    }

    @PostMapping("/{id}/delete")
    public String deleteMyMessage(@PathVariable Long id, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        ChatMessage message = messageRepository.findById(id).orElse(null);
        if (message != null && message.getAuthor().getId().equals(user.getId())) {
            messageRepository.delete(message);
        }
        return "redirect:/chat/my";
    }

    @GetMapping("/api/latest")
    @ResponseBody
    public List<ChatMessageDto> getLatestMessages() {
        return messageRepository.findTop50ByOrderBySentAtDesc().stream()
                .map(msg -> {
                    ChatMessageDto dto = new ChatMessageDto();
                    dto.setId(msg.getId());
                    dto.setContent(msg.getContent());
                    dto.setAuthorName(msg.getAuthor().getUsername());
                    dto.setSentAt(msg.getSentAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    return dto;
                })
                .collect(Collectors.toList());
    }
}