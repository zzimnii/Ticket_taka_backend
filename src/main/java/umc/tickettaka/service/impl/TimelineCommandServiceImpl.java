package umc.tickettaka.service.impl;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.domain.Project;
import umc.tickettaka.domain.Timeline;
import umc.tickettaka.repository.TimelineRepository;
import umc.tickettaka.service.ImageUploadService;
import umc.tickettaka.service.ProjectQueryService;
import umc.tickettaka.service.TimelineCommandService;
import umc.tickettaka.web.dto.request.TimelineRequestDto;

@Service
@RequiredArgsConstructor
public class TimelineCommandServiceImpl implements TimelineCommandService {


    private final TimelineRepository timelineRepository;
    private final ProjectQueryService projectQueryService;
    private final ImageUploadService imageUploadService;

    @Override
    @Transactional
    public Timeline createTimeline(Long projectId, MultipartFile image, TimelineRequestDto.CreateTimelineDto request) throws IOException {
        Project project = projectQueryService.findById(projectId);
        String imageUrl = imageUploadService.uploadImage(image);

        Timeline timeline = Timeline.builder()
            .name(request.getName())
            .imageUrl(imageUrl)
            .project(project)
            .build();

        return timelineRepository.save(timeline);
    }

}
