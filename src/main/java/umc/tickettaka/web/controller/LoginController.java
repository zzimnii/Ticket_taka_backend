package umc.tickettaka.web.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class LoginController {


    @GetMapping("/oauth2/sign-up")
    public String loadOAuthSignUp() {
        log.info("oauth2/signUp");

        return "signUp";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
