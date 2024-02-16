package umc.tickettaka.service;

import java.util.List;
import umc.tickettaka.domain.Project;
import umc.tickettaka.domain.Timeline;

public interface ProjectQueryService {

    List<Project> findAllByTeamId(Long teamId);
    Project findById(Long id);
    Project findByTimeline(Timeline timeline);
}
