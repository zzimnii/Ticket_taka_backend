package umc.tickettaka.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Team;
import umc.tickettaka.web.dto.request.TeamRequestDto;

public interface TeamCommandService {
    Team createTeam(Member member, MultipartFile image, TeamRequestDto.TeamDto request) throws IOException;
}