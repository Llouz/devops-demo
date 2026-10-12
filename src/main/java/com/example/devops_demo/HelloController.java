package com.example.devops_demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final VisitRepository visits;

    public HelloController(VisitRepository visits) {
        this.visits = visits;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from my DevOps project!";
    }

    @GetMapping("/visits")
    public String visits() {
        visits.save(new Visit());
        return "Total visits: " + visits.count();
    }
}