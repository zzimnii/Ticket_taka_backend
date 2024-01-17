package umc.tickettaka.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.domain.Timeline;
import umc.tickettaka.web.dto.request.TimelineRequestDto;

public interface TimelineCommandService {

    Timeline createTimeline(Long projectId, MultipartFile image, TimelineRequestDto.CreateTimelineDto request) throws IOException;

}
