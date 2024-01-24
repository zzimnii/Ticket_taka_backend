package umc.tickettaka.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.tickettaka.domain.Timeline;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
import umc.tickettaka.repository.TimelineRepository;
import umc.tickettaka.service.TimelineQueryService;

@Service
@RequiredArgsConstructor
public class TimelineQueryServiceImpl implements TimelineQueryService {

    private final TimelineRepository timelineRepository;

    @Override
    public List<Timeline> findAllByProjectId(Long projectId) {
        return timelineRepository.findAllByProjectIdOrderByUpdatedTime(projectId);
    }

    @Override
    public Timeline findById(Long timelineId) {
        return timelineRepository.findById(timelineId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.TIMELINE_NOT_FOUND));
    }
}
