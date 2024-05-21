package com.project.nexushub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class NexusHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexusHubApplication.class, args);
    }

    @Controller
    public static class MainController {

        @RequestMapping(value = {"/", "/{path:[^\\.]*}"})
        public String redirect() {
            return "forward:/index.html";
        }
    }
}
