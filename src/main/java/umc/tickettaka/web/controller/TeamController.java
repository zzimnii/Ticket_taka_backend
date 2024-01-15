package umc.tickettaka.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.tickettaka.converter.TeamConverter;
import umc.tickettaka.domain.Team;
import umc.tickettaka.payload.ApiResponse;
import umc.tickettaka.web.dto.teamDto.RequestDto;
import umc.tickettaka.web.dto.teamDto.ResponseDto;
import umc.tickettaka.web.repository.TeamRepository;
import umc.tickettaka.web.service.TeamCommandService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamCommandService teamCommandService;
    private final TeamRepository teamRepository;

    @PostMapping("/create")
    @Operation(summary = "Team 생성 API", description = "Team 생성하는 API")
    public ApiResponse<ResponseDto.TeamResponseDto> createTeam(@RequestBody RequestDto.TeamRequestDto request) {
        Team team = teamCommandService.createTeam(request);
        return ApiResponse.onSuccess(TeamConverter.toResultDto(team));
    }

    @GetMapping("/")
    @Operation(summary = "생성된 Team 조회 API", description = "생성된 Team 조회하는 API")
    public ApiResponse<List<Team>> getTeamList() {
        return ApiResponse.onSuccess(teamRepository.findAll());
    }

    @GetMapping("/{teamsId}")
    @Operation(summary = "특정 팀 조회 API",description = "특정 팀 조회하는 API")
    @Parameters({
            @Parameter(name = "teamsId", description = "팀의 아이디, path variable 입니다.")
    })
    public ApiResponse<Optional<Team>> getTeam(@PathVariable(name = "teamsId") Long teamsId) {
        return ApiResponse.onSuccess(teamRepository.findById(teamsId));
    }
}