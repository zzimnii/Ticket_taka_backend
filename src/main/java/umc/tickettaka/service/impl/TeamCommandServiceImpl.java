package umc.tickettaka.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.converter.TeamConverter;
import umc.tickettaka.domain.Invitation;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Team;
import umc.tickettaka.domain.enums.Color;
import umc.tickettaka.domain.mapping.MemberTeam;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
import umc.tickettaka.repository.InvitationRepository;
import umc.tickettaka.repository.MemberTeamRepository;
import umc.tickettaka.service.ImageUploadService;
import umc.tickettaka.service.MemberQueryService;
import umc.tickettaka.service.TeamCommandService;
import umc.tickettaka.service.TeamQueryService;
import umc.tickettaka.web.dto.request.MemberTeamRequestDto;
import umc.tickettaka.web.dto.request.TeamRequestDto;
import umc.tickettaka.repository.TeamRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamCommandServiceImpl implements TeamCommandService {

    private final TeamRepository teamRepository;
    private final ImageUploadService imageUploadService;
    private final MemberTeamRepository memberTeamRepository;
    private final MemberQueryService memberQueryService;
    private final TeamQueryService teamQueryService;
    private final InvitationRepository invitationRepository;

    @Override
    @Transactional
    public Team createTeam(Member member, MultipartFile image, TeamRequestDto.CreateTeamDto request) throws IOException {
        String imageUrl = imageUploadService.uploadImage(image);
        Team team = TeamConverter.toTeam(request, imageUrl);
        Team newTeam = teamRepository.save(team);

        setMemberTeam(member, newTeam, request.getInvitedUsernameList());
        return newTeam;
    }

    private void setMemberTeam(Member creator, Team team, List<String> invitedUsernameList) {
        sendInvitationsForMemberList(creator, team, invitedUsernameList);
        MemberTeam creatorMemberTeam = MemberTeam.builder().team(team).member(creator).color(Color.getRandomColor()).build();
        memberTeamRepository.save(creatorMemberTeam);
    }

    private void sendInvitationsForMemberList(Member sender, Team team, List<String> invitedUsernameList) {
        List<Invitation> invitationList = invitedUsernameList.stream()
                .map(username -> Invitation.builder()
                        .sender(sender)
                        .receiver(memberQueryService.findByUsername(username))
                        .team(team)
                        .build())
                .collect(Collectors.toList());
        invitationRepository.saveAll(invitationList);
    }

    @Override
    @Transactional
    public Team updateTeam(Long id, MultipartFile image, TeamRequestDto.CreateTeamDto updateTeamDto) throws IOException {
        Team team = teamQueryService.findTeam(id);
        String imageUrl = team.getImageUrl();

        if (image != null) {
            imageUrl = imageUploadService.uploadImage(image);
        }

        if (updateTeamDto == null) {
            throw new GeneralException(ErrorStatus.INVALID_UPDATE_TEAM, "변경 NAME이 입력되지 않았습니다");
        }
        return team.update(imageUrl, updateTeamDto);
    }

    @Override
    public void deleteTeam(Long id) throws IOException {
        Team team = teamQueryService.findTeam(id);
        teamRepository.delete(team);
    }

    @Override
    @Transactional
    public void updateMemberTeamColor(Member member, Long teamsId, MemberTeamRequestDto.UpdateColorDto request) {
        Team team = teamQueryService.findTeam(teamsId);

        MemberTeam memberTeam = memberTeamRepository.findByTeamAndMember(team, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_TEAM_NOT_FOUND));

        if (isColorAlreadyUsedInTeam(team, request.getColor())) {
            throw new GeneralException(ErrorStatus.COLOR_ALREADY_USED_IN_TEAM);
        }

        memberTeam.updateColor(request);
        memberTeamRepository.save(memberTeam);
    }

    public boolean isColorAlreadyUsedInTeam(Team team, Color color) {
        return team.getMemberTeamList().stream()
                .anyMatch(memberTeam -> memberTeam.getColor() == color);
    }
}
