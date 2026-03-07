package ru.kpfu.itis.amirova.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.amirova.dto.UserDto;
import ru.kpfu.itis.amirova.service.HelloService;
import ru.kpfu.itis.amirova.service.UserService;

import java.util.List;

@RestController
public class HelloController {

    private final HelloService helloService;
    private final UserService userService;

    public HelloController(HelloService helloService, UserService userService) {
        this.helloService = helloService;
        this.userService = userService;
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(required = false, name = "name") String name) {
        return helloService.sayHello(name);
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> findAll() {
        return userService.findAll();
    }
}