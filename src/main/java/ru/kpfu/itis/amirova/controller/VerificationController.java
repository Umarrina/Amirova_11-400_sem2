package ru.kpfu.itis.amirova.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.amirova.model.User;
import ru.kpfu.itis.amirova.repository.UserRepository;

@Controller
public class VerificationController {

    private final UserRepository userRepository;

    public VerificationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/verify_page")
    public String showVerifyPage() {
        return "verify";
    }

    @PostMapping("/verify")
    public String verify(@RequestParam("code") String code, Model model) {
        User user = userRepository.findByVerificationCode(code)
                .orElse(null);
        if (user == null) {
            model.addAttribute("error", "Неверный код подтверждения");
            return "verify";
        }
        user.setEnabled(true);
        user.setVerificationCode(null);
        userRepository.save(user);
        return "redirect:/login?verified";
    }

    @GetMapping("/verification")
    public String verifyByLink(@RequestParam("code") String code) {
        User user = userRepository.findByVerificationCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Invalid code"));
        user.setEnabled(true);
        user.setVerificationCode(null);
        userRepository.save(user);
        return "redirect:/login?verified";
    }
}