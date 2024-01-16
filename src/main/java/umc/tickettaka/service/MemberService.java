package umc.tickettaka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import umc.tickettaka.config.security.jwt.JwtToken;
import umc.tickettaka.config.security.jwt.JwtTokenProvider;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.enums.ProviderType;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
import umc.tickettaka.repository.MemberRepository;
import umc.tickettaka.web.dto.request.SignRequestDto;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final CustomUserDetailService customUserDetailService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;


    @Transactional
    public JwtToken signIn(String username, String password) {
        // DB에서 회원정보를 가져온 후 -> CustomUserDetail로 변환한 객체를 받는다.
        UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

        // 비밀번호가 다른 경우 UNAUTHORIZED 응답을 보낸다.
        if (username == null || password == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        // 1.사용자가 아이디와 비밀번호를 입력하면 UsernamePasswordAuthentication 토큰을 생성한다.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

    @Transactional
    public Member save(SignRequestDto.SignUpDto signUpDto) {
        // parse values
        String username = signUpDto.getUsername();
        String password = signUpDto.getPassword();
        String email = signUpDto.getEmail();
        String imageUrl = signUpDto.getImageUrl();
        String providerType = signUpDto.getProviderType();
        String providerId = signUpDto.getProviderId();

        ProviderType providerTypeEnum = null;
        if (providerType != null) providerTypeEnum = ProviderType.valueOf(providerType);

        // duplicate check
        checkDuplicateMember(username, email);

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .imageUrl(imageUrl)
                .providerType(providerTypeEnum)
                .providerId(providerId)
                .build();

        return memberRepository.save(member);

    }

    private void checkDuplicateMember(String username, String email) {
        Optional<Member> memberByUsername = memberRepository.findByUsername(username);
        if(memberByUsername.isPresent()) throw new GeneralException(ErrorStatus.MEMBER_ALREADY_EXIST, "멤버가 이미 존재합니다.");
        Optional<Member> memberByEmail = memberRepository.findByEmail(email);
        if(memberByEmail.isPresent()) throw new GeneralException(ErrorStatus.MEMBER_ALREADY_EXIST, "멤버가 이미 존재합니다.");
    }

    public Member findByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND, "해당 email을 가진 회원이 없습니다."));

        return member;

    }

    public Member findByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND, "해당 username을 가진 회원이 없습니다."));

        return member;
    }
}
