package ru.kpfu.itis.amirova.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.amirova.dto.CreateUserDto;
import ru.kpfu.itis.amirova.service.UserService;

@Controller
public class SignUpController {

    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sign_up")
    public String showSignUpForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Ошибка регистрации. Возможно, пользователь уже существует.");
        }
        return "sign_up";
    }

    @PostMapping("/sign_up")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("email") String email) {
        try {
            userService.createUser(new CreateUserDto(username, password, email));
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            return "redirect:/sign_up?error";
        }
    }
}