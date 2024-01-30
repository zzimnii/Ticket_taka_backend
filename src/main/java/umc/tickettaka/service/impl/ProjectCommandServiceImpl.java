package umc.tickettaka.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.converter.ProjectConverter;
import umc.tickettaka.domain.Link;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Project;
import umc.tickettaka.domain.Team;
import umc.tickettaka.domain.mapping.MemberTeam;
import umc.tickettaka.repository.ProjectRepository;
import umc.tickettaka.service.*;
import umc.tickettaka.web.dto.request.ProjectRequestDto;
import umc.tickettaka.web.dto.response.ProjectResponseDto;
import umc.tickettaka.web.dto.response.TicketResponseDto;

@Service
@RequiredArgsConstructor
public class ProjectCommandServiceImpl implements ProjectCommandService {

    private final TeamQueryService teamQueryService;
    private final ImageUploadService imageUploadService;
    private final ProjectRepository projectRepository;
    private final ProjectQueryService projectQueryService;
    private final TicketCommandService ticketCommandService;

    @Override
    @Transactional
    public Project createProject(Long teamId, MultipartFile image, ProjectRequestDto.CreateProjectDto request) throws IOException {
        Team team = teamQueryService.findTeam(teamId);
        String imageUrl = imageUploadService.uploadImage(image);

        Project project = Project.builder()
            .name(request.getName())
            .description(request.getDescription())
            .imageUrl(imageUrl)
            .team(team)
            .build();

        return projectRepository.save(project);
    }

    @Override
    @Transactional
    public ProjectResponseDto.ProjectMainDto getProjectMainDto(Long teamId, Long projectId) {
        Team team = teamQueryService.findTeam(teamId);
        Project project = projectQueryService.findById(projectId);
        List<TicketResponseDto.MemberAchieveLevelDto> memberAchieveLevelDtoList = new ArrayList<>();

        for(MemberTeam memberTeam : team.getMemberTeamList()) {
            Member member = memberTeam.getMember();

            TicketResponseDto.MemberAchieveLevelDto memberAchieveLevelDto = ticketCommandService.showMemberAchieve(member,teamId);
            memberAchieveLevelDtoList.add(memberAchieveLevelDto);
        }

        List<String> linkList = project.getLinkList().stream()
            .map(Link::getUrl).toList();

        return ProjectConverter.toShowProjectMainDto(team.getName(), project.getName(), memberAchieveLevelDtoList, project.getDescription(), linkList);
    }

    @Override
    @Transactional
    public Project updateProject(Long teamId, Long projectId, MultipartFile image, ProjectRequestDto.UpdateProjectDto updateProjectDto) throws IOException {
        Project project = projectQueryService.findById(projectId);
        String imageUrl = project.getImageUrl();

        if (image != null) {
            imageUrl = imageUploadService.uploadImage(image);
        }
//        if (updateProjectDto == null) {
//            throw new GeneralException(ErrorStatus.INVALID_UPDATE_INFO, "변경 NAME과 DESCRIPTION이 입력되지 않았습니다.");
//        }
        return project.update(imageUrl, updateProjectDto);
    }

    @Override
    @Transactional
    public void deleteProject(Long teamId, Long projectId) {

        Project project = projectQueryService.findById(projectId);
        projectRepository.delete(project);
    }
}
