package umc.tickettaka.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/health")
    public String healthCheck() {
        return "Ticket-Taka Fighting❤️‍🔥";
    }
}
