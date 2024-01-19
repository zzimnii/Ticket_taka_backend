package umc.tickettaka.service.impl;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.converter.TeamConverter;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Project;
import umc.tickettaka.domain.Team;
import umc.tickettaka.domain.mapping.MemberTeam;
import umc.tickettaka.domain.mapping.ScheduleTeam;
import umc.tickettaka.repository.MemberTeamRepository;
import umc.tickettaka.service.ImageUploadService;
import umc.tickettaka.service.MemberQueryService;
import umc.tickettaka.service.TeamCommandService;
import umc.tickettaka.service.TeamQueryService;
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

    @Override
    @Transactional
    public Team createTeam(Member member, MultipartFile image, TeamRequestDto.TeamDto request) throws IOException {
        String imageUrl = imageUploadService.uploadImage(image);
        Team team = TeamConverter.toTeam(request, imageUrl);
        Team newTeam = teamRepository.save(team);
        setMemberTeam(member, newTeam, request.getInvitedUsernameList());

        return newTeam;
    }

    private void setMemberTeam(Member creator, Team team, List<String> invitedUsernameList) {
        List<MemberTeam> memberTeamList = new java.util.ArrayList<>(invitedUsernameList.stream()
            .map(username ->
                MemberTeam.builder()
                    .team(team)
                    .member(memberQueryService.findByUsername(username))
                    .build()).toList());

        memberTeamList.add(MemberTeam.builder().team(team).member(creator).build());
        memberTeamRepository.saveAll(memberTeamList);
    }

    @Override
    public Team updateTeam(Long id, MultipartFile image, TeamRequestDto.TeamDto request) throws IOException {
        Team team = teamQueryService.findTeam(id);
        String imageUrl = team.getImageUrl();
        List<MemberTeam> memberTeamList = team.getMemberTeamList();
        List<Project> projectList = team.getProjectList();
        List<ScheduleTeam> scheduleTeamList = team.getScheduleTeamList();

        if (image != null) {
            imageUrl = imageUploadService.uploadImage(image);
        }

        team = Team.builder()
                .id(id)
                .name(request.getName())
                .imageUrl(imageUrl)
                .memberTeamList(memberTeamList)
                .projectList(projectList)
                .scheduleTeamList(scheduleTeamList)
                .build();

        return teamRepository.save(team);
    }

    @Override
    public void deleteTeam(Long id) throws IOException {
        Team team = teamQueryService.findTeam(id);
        teamRepository.delete(team);
    }
}
