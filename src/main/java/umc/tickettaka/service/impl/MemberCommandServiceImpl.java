package umc.tickettaka.service.impl;

import java.util.*;
import java.io.IOException;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.config.security.jwt.JwtToken;
import umc.tickettaka.config.security.jwt.JwtTokenProvider;
import umc.tickettaka.converter.MemberConverter;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.RefreshToken;
import umc.tickettaka.domain.Team;
import umc.tickettaka.domain.mapping.MemberTeam;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
import umc.tickettaka.repository.MemberRepository;
import umc.tickettaka.service.*;
import umc.tickettaka.web.dto.common.CommonMemberDto;
import umc.tickettaka.web.dto.request.MemberRequestDto;
import umc.tickettaka.web.dto.request.SignRequestDto;
import umc.tickettaka.web.dto.response.SignResponseDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final ImageUploadService imageUploadService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final TeamQueryService teamQueryService;
    private final RefreshTokenService refreshTokenService;

    private final BlackListService blackListService;

    @Override
    @Transactional
    public JwtToken signIn(SignRequestDto.SignInDto signInDto) {
        // DB에서 회원정보를 가져온 후 -> CustomUserDetail로 변환한 객체를 받는다.
        String username = signInDto.getUsername();
        String password = signInDto.getPassword();
        Boolean keepStatus = signInDto.getKeepStatus();
        log.info("request username = {}, password = {}, keeppStatus = {}", username, password, keepStatus);

        // form 에 null이 들어오는 경우 잘못된 정보라는 exception을 던진다.
        if (username == null || password == null) {
            throw new GeneralException(ErrorStatus.MEMBER_WRONG_INFORMATION, "form에 null인 정보가 들어왔습니다.");
        }

        // 1.사용자가 아이디와 비밀번호를 입력하면 UsernamePasswordAuthentication 토큰을 생성한다.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication, username, keepStatus);

        // 3. refreshToken Redis 적용
        refreshTokenService.saveRefreshToken(jwtToken.getAccessToken(), jwtToken.getRefreshToken());

        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

    @Override
    @Transactional
    public Member save(SignRequestDto.SignUpDto signUpDto) {
        Member member = null;
        // sns 로그인이 아닌 일반 로그인인 경우
        if (signUpDto.getProviderType() == null) {
            // null check front에서 1차적으로 해야하지만, backend에서 한번 더 체크
            if(signUpDto.getUsername() == null || signUpDto.getPassword() == null || signUpDto.getPassword2() == null)
                throw new GeneralException(ErrorStatus.BAD_REQUEST, "입력 값 중에 null이 있습니다.");

            // duplicate check
            String username = signUpDto.getUsername();
            checkDuplicateMember(username);

            // check password == password2
            if(!Objects.equals(signUpDto.getPassword(), signUpDto.getPassword2()))
                throw new GeneralException(ErrorStatus.MEMBER_WRONG_INFORMATION, "password와 passsword 확인이 다릅니다.");

            // password encoding
            String password = signUpDto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);

            // dto to member
            member = MemberConverter.toMember(signUpDto, encodedPassword);
        }
        // sns 로그인인 경우
        else {
            member = MemberConverter.toMember(signUpDto, null);
        }
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

    @Override
    public String getAccessToken(String expiredAccessToken) {
        // 1. expiredAccessToken 에서 필요한 정보를 가져온다
        Claims claims = jwtTokenProvider.parseClaims(expiredAccessToken);

        String username = claims.get("username", String.class);
        String provider = claims.get("provider", String.class);
        String email = claims.get("email", String.class);
        String roles = claims.get("auth", String.class);

        // 2-1. 일반 로그인인 경우
        if (provider == null) {

            // refreshToken 만료 체크
            RefreshToken refreshToken = refreshTokenService.findByAccessToken(expiredAccessToken);
            checkRefreshTokenExpire(refreshToken.getRefreshToken());

            // 3. jwt 토큰을 만들어 return 한다
            return jwtTokenProvider.recreateAccessToken(username, email, null, roles);
        }
        // 2-2. sns 로그인인 경우
        else {

            // refreshToken 만료 체크
            RefreshToken refreshToken = refreshTokenService.findByAccessToken(expiredAccessToken);
            checkRefreshTokenExpire(refreshToken.getRefreshToken());

            // 3. jwt 토큰을 만들어 return 한다.
            return jwtTokenProvider.recreateAccessToken(username, email, provider, roles);
        }

    }

    @Override
    public SignResponseDto.SignOutResultDto signOut(SignRequestDto.SignOutDto signOutDto) {
        String accessToken = signOutDto.getAccessToken();

        blackListService.saveBlackList(accessToken);
        SignResponseDto.SignOutResultDto resultDto = SignResponseDto.SignOutResultDto.builder()
                .accessToken(accessToken)
                .build();

        refreshTokenService.deleteRefreshToken(accessToken);

        return resultDto;
    }

    private void checkRefreshTokenExpire(String refreshToken) {
        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        Date expiration = claims.getExpiration();

        if (expiration.before(new Date())) {
            throw new GeneralException(ErrorStatus.EXPIRED_REFRESH_TOKEN, "refreshToken이 만료되었습니다.");
        }

    }

    private void UpdateDuplicateCheck(MemberRequestDto.UpdateDto memberUpdateDto, Member member) {
        String username = memberUpdateDto.getUsername();
        Optional<Member> foundUsername = null;
        if (username != null) {
            foundUsername = memberRepository.findByUsername(username);

            if(foundUsername.isPresent())
                throw new GeneralException(ErrorStatus.USERNAME_ALREADY_EXISTS, "해당 멤버 username이 이미 존재합니다.");
        }


        String password = memberUpdateDto.getPassword();

        if(member.getPassword() != null && passwordEncoder.matches(password, member.getPassword()))
            throw new GeneralException(ErrorStatus.PASSWORD_SAME, "변경할 password가 기존과 같습니다");
    }

    private void checkDuplicateMember(String username) {
        Optional<Member> memberByName = memberRepository.findByUsername(username);
        if (memberByName.isPresent()) {
            throw new GeneralException(ErrorStatus.MEMBER_ALREADY_EXIST, "멤버가 이미 존재합니다.");
        }
    }

    @Override
    @Transactional
    public CommonMemberDto.ShowMemberProfileDto getMemberProfileDto(Member member, Long teamId) {
        Team team = teamQueryService.findTeam(teamId);
        String memberName = member.getUsername();
        String imageUrl = member.getImageUrl();
        MemberTeam memberTeam = team.getMemberTeamList().stream()
                .filter(mt -> mt.getMember().equals(member))
                .findFirst()
                .orElse(null);

        if (memberTeam == null) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }
        String memberHex = memberTeam.getColor().getHex();

        CommonMemberDto.ShowMemberProfileDto showMemberProfileDto = CommonMemberDto.ShowMemberProfileDto.builder()
                .imageUrl(imageUrl)
                .name(memberName)
                .memberHex(memberHex)
                .build();

        return showMemberProfileDto;
    }

    @Override
    @Transactional
    public CommonMemberDto.ShowMemberProfileListDto getCommonMemberDto (Long teamId) {
        Team team = teamQueryService.findTeam(teamId);
        List<CommonMemberDto.ShowMemberProfileDto> showMemberProfileDtoList = new ArrayList<>();

        for (MemberTeam memberTeam : team.getMemberTeamList()) {
            Member member = memberTeam.getMember();

            CommonMemberDto.ShowMemberProfileDto showMemberProfileDto = getMemberProfileDto(member,teamId);

            showMemberProfileDtoList.add(showMemberProfileDto);
        }

        return CommonMemberDto.ShowMemberProfileListDto.builder()
                .showMemberProfileDtoList(showMemberProfileDtoList)
                .build();
    }
}
