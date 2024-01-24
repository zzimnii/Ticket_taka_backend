package umc.tickettaka.service.impl;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.converter.FileConverter;
import umc.tickettaka.converter.TicketConverter;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Timeline;
import umc.tickettaka.domain.mapping.TicketReviewer;
import umc.tickettaka.domain.ticket.File;
import umc.tickettaka.domain.ticket.Ticket;
import umc.tickettaka.repository.FileRepository;
import umc.tickettaka.repository.TicketRepository;
import umc.tickettaka.repository.TicketReviewerRepository;
import umc.tickettaka.service.ImageUploadService;
import umc.tickettaka.service.MemberQueryService;
import umc.tickettaka.service.TicketCommandService;
import umc.tickettaka.service.TicketQueryService;
import umc.tickettaka.service.TimelineQueryService;
import umc.tickettaka.web.dto.request.TicketRequestDto.CreateTicketDto;
import umc.tickettaka.web.dto.request.TicketRequestDto.DeleteTicketDto;

@Service
@RequiredArgsConstructor
public class TicketCommandServiceImpl implements TicketCommandService {

    private final TimelineQueryService timelineQueryService;
    private final ImageUploadService imageUploadService;
    private final FileRepository fileRepository;
    private final MemberQueryService memberQueryService;
    private final TicketReviewerRepository ticketReviewerRepository;
    private final TicketQueryService ticketQueryService;
    private final TicketRepository ticketRepository;

    @Override
    @Transactional
    public Ticket createTicket(Long timelineId, List<MultipartFile> files, CreateTicketDto request) throws IOException {

        Timeline timeline = timelineQueryService.findById(timelineId);
        Member worker = memberQueryService.findByUsername(request.getWorkerName());
        Long sequence = ticketRepository.countByTimeline(timeline) + 1;
        Ticket ticket = TicketConverter.toTicket(timeline, worker, sequence, request);
        Ticket newTicket = ticketRepository.save(ticket);
        setReviewers(request, ticket);
        if (files != null) {
            setFiles(files, ticket);
        }

        return newTicket;
    }

    private void setReviewers(CreateTicketDto request, Ticket ticket) {
        List<Member> reviewers = request.getReviewerNameList().stream()
            .map(memberQueryService::findByUsername).toList();
        List<TicketReviewer> ticketReviewerList = reviewers.stream()
            .map(reviewer -> TicketReviewer.builder()
                .member(reviewer)
                .ticket(ticket)
                .build()).toList();
        ticketReviewerRepository.saveAll(ticketReviewerList);
    }

    private void setFiles(List<MultipartFile> files, Ticket ticket) throws IOException {
        for (MultipartFile file : files) {
            String fileUrl = imageUploadService.uploadImage(file);
            File fileEntity = FileConverter.toFile(fileUrl, ticket);

            fileRepository.save(fileEntity);
        }
    }

    @Override
    @Transactional
    public void deleteTicket(DeleteTicketDto request) {
        Ticket ticket = ticketQueryService.findById(request.getTicketId());
        ticketRepository.delete(ticket);

        List<TicketReviewer> ticketReviewerList = ticketReviewerRepository.findAllByTicket(ticket);
        ticketReviewerRepository.deleteAll(ticketReviewerList);
    }

}
