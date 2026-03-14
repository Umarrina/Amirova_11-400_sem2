package ru.kpfu.itis.amirova.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.amirova.model.Role;
import ru.kpfu.itis.amirova.model.User;
import ru.kpfu.itis.amirova.repository.RoleRepository;
import ru.kpfu.itis.amirova.repository.UserRepository;

import java.util.List;

@Controller
public class SignUpController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/sign_up")
    public String showSignUpForm(@RequestParam(value = "error", required = false) String error, Model model ) {
        if (error != null) {
            model.addAttribute("error", "Пользователь с таким именем уже существует");
        }
        return "sign_up";
    }

    @PostMapping("/sign_up")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password) {

        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/sign_up?error";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(List.of(userRole));
        userRepository.save(user);
        return "redirect:/login";
    }
}
