package umc.tickettaka.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.tickettaka.config.security.jwt.JwtToken;
import umc.tickettaka.web.dto.jwtDto.RequestDto;
import umc.tickettaka.service.MemberService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * login, 회원가입
     */
    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody RequestDto.SignInDto signInDto) {
        String username = signInDto.getUsername();
        String password = signInDto.getPassword();

        JwtToken jwtToken = memberService.signIn(username , password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}", jwtToken.getAccessToken());
        return jwtToken;
    }

}
