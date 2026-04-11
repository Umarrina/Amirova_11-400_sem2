package ru.kpfu.itis.amirova.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class HelloServiceTest {
    private final HelloService helloService = new HelloService();

    @Test
    void sayHello_WithName() {
        assertThat(helloService.sayHello("World")).isEqualTo("Hello, World");
    }

    @Test
    void sayHello_WithNull() {
        assertThat(helloService.sayHello(null)).isEqualTo("Hello, null");
    }
}