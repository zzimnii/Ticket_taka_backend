package umc.tickettaka.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.tickettaka.converter.TeamConverter;
import umc.tickettaka.domain.Team;
import umc.tickettaka.payload.ApiResponse;
import umc.tickettaka.repository.TeamRepository;
import umc.tickettaka.service.TeamQueryService;
import umc.tickettaka.web.dto.request.TeamRequestDto;
import umc.tickettaka.web.dto.response.TeamResponseDto;
import umc.tickettaka.service.TeamCommandService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamCommandService teamCommandService;
    private final TeamQueryService teamQueryService;
    private final TeamRepository teamRepository;

    @PostMapping("/create")
    @Operation(summary = "Team 생성 API", description = "Team 생성하는 API")
    public ApiResponse<TeamResponseDto.TeamDto> createTeam(@RequestBody TeamRequestDto.TeamDto request) {
        Team team = teamCommandService.createTeam(request);
        return ApiResponse.onSuccess(TeamConverter.toTeamResultDto(team));
    }

    @GetMapping("/")
    @Operation(summary = "생성된 Team 조회 API", description = "생성된 Team 조회하는 API")
    public ApiResponse<TeamResponseDto.TeamListDto> getTeamList() {
        List<Team> teamList = teamRepository.findAll();
        TeamResponseDto.TeamListDto teamListDto = TeamConverter.toTeamListDto(teamList);
        return ApiResponse.onSuccess(teamListDto);
    }

    @GetMapping("/{teamsId}")
    @Operation(summary = "특정 팀 조회 API",description = "특정 팀 조회하는 API")
    @Parameters({
            @Parameter(name = "teamsId", description = "팀의 아이디, path variable 입니다.")
    })
    public ApiResponse<TeamResponseDto.TeamDto> getTeam(@PathVariable(name = "teamsId") Long teamsId ) {
        Team team = teamQueryService.findTeam(teamsId).get();
        return ApiResponse.onSuccess(TeamConverter.toTeamResultDto(team));
    }
}