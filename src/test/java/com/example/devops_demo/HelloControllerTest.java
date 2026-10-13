package com.example.devops_demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class HelloControllerTest {

    @Test
    void helloReturnsExpectedMessage() {
        HelloController controller = new HelloController(null);
        assertEquals("Hello from my DevOps project!", controller.hello());    }
}