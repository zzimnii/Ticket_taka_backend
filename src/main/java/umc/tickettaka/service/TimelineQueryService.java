package umc.tickettaka.service;

import java.util.List;
import umc.tickettaka.domain.Timeline;

public interface TimelineQueryService {

    List<Timeline> findAllByProjectId(Long projectId);
}
