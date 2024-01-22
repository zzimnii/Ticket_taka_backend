package umc.tickettaka.service.impl;

import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.config.security.jwt.JwtToken;
import umc.tickettaka.config.security.jwt.JwtTokenProvider;
import umc.tickettaka.converter.MemberConverter;
import umc.tickettaka.domain.Member;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
import umc.tickettaka.repository.MemberRepository;
import umc.tickettaka.config.security.jwt.CustomUserDetailService;
import umc.tickettaka.service.ImageUploadService;
import umc.tickettaka.service.MemberCommandService;
import umc.tickettaka.web.dto.request.MemberRequestDto;
import umc.tickettaka.web.dto.request.SignRequestDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final CustomUserDetailService customUserDetailService;
    private final ImageUploadService imageUploadService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public JwtToken signIn(SignRequestDto.SignInDto signInDto) {
        // DB에서 회원정보를 가져온 후 -> CustomUserDetail로 변환한 객체를 받는다.
        String username = signInDto.getUsername();
        String password = signInDto.getPassword();
        log.info("request username = {}, password = {}", username, password);
        UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

        // 비밀번호가 다른 경우 UNAUTHORIZED 응답을 보낸다.
        if (username == null || password == null) {
            throw new GeneralException(ErrorStatus.MEMBER_WRONG_INFORMATION);
        }

        // 1.사용자가 아이디와 비밀번호를 입력하면 UsernamePasswordAuthentication 토큰을 생성한다.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication, username);
        log.info("jwtToken accessToken = {}", jwtToken.getAccessToken());
        return jwtToken;
    }

    @Override
    @Transactional
    public Member save(SignRequestDto.SignUpDto signUpDto) {
        // duplicate check
        String username = signUpDto.getUsername();
        String email = signUpDto.getEmail();
        checkDuplicateMember(username, email);

        // password encoding
        String password = signUpDto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        // dto to member
        Member member = MemberConverter.toMember(signUpDto, encodedPassword);
        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member updateMember(Long memberId, MultipartFile image, MemberRequestDto.UpdateDto memberUpdateDto) throws IOException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND, "update할 멤버를 찾을 수 없습니다."));


        UpdateDuplicateCheck(memberUpdateDto, member);

        String imageUrl = member.getImageUrl();
        if (image != null) {
            imageUrl = imageUploadService.uploadImage(image);
        }

        String password = memberUpdateDto.getPassword();
        if (password != null) {
            password = passwordEncoder.encode(memberUpdateDto.getPassword());
        }

        return member.update(imageUrl, memberUpdateDto, password);
    }

    private void UpdateDuplicateCheck(MemberRequestDto.UpdateDto memberUpdateDto, Member member) {
        String username = memberUpdateDto.getUsername();
        Optional<Member> foundUsername = memberRepository.findByUsername(username);

        if(foundUsername.isPresent())
            throw new GeneralException(ErrorStatus.USERNAME_ALREADY_EXISTS, "해당 멤버 username이 이미 존재합니다.");

        String email = memberUpdateDto.getEmail();
        Optional<Member> foundEmail = memberRepository.findByEmail(email);
        if(foundEmail.isPresent())
            throw new GeneralException(ErrorStatus.EMAIL_ALREADY_EXISTS, "해당 멤버 email이 이미 존재합니다.");

        String password = memberUpdateDto.getPassword();
        if(passwordEncoder.matches(password, member.getPassword()))
            throw new GeneralException(ErrorStatus.PASSWORD_SAME, "변경할 password가 기존과 같습니다");
    }

    private void checkDuplicateMember(String username, String email) {
        Optional<Member> memberByName = memberRepository.findByUsername(username);
        if (memberByName.isPresent()) {
            throw new GeneralException(ErrorStatus.MEMBER_ALREADY_EXIST, "멤버가 이미 존재합니다.");
        }
        Optional<Member> memberByEmail = memberRepository.findByEmail(email);
        if (memberByEmail.isPresent()) {
            throw new GeneralException(ErrorStatus.MEMBER_ALREADY_EXIST, "멤버가 이미 존재합니다.");
        }
    }
}
