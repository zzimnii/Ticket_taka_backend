package umc.tickettaka.domain;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.tickettaka.domain.common.BaseEntity;
import umc.tickettaka.domain.enums.ProviderType;
import umc.tickettaka.domain.mapping.MemberTeam;
import umc.tickettaka.domain.ticket.Ticket;
import umc.tickettaka.web.dto.request.MemberRequestDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 기획상 로그인 입력시 아이디와 같음
    // https://www.figma.com/file/PqaiMaXZED8IeWM4CD4Riw/Ticket-Taka?type=design&node-id=55-929&mode=design&t=zIGwjIheIWnB6A2r-0
    @Column(length = 50)
    private String username;

    // nickName
    @Column(length = 50)
    private String name;

    @Column(length = 500)
    private String password;

    @Column(length = 500)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    private String email;

    private String deviceToken; // 기기 연동을 위한 토큰

    private String refreshToken;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    // 연관관계
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> ticketList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberTeam> memberTeamList = new ArrayList<>();


    public Member update(String imageUrl, MemberRequestDto.UpdateDto memberUpdateDto, String password) {

        this.imageUrl = imageUrl;

        String updateName = memberUpdateDto.getName();
        if(updateName != null) this.name = updateName;

        String updateUsername = memberUpdateDto.getUsername();
        if(updateUsername != null) this.username = updateUsername;

        if(password != null) this.password = password;

        return this;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

//    @OneToMany(mappedBy = "member")
//    private List<MemberTeam> memberTeamList = new ArrayList<>();
}
