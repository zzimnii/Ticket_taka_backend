package umc.tickettaka.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.converter.FeedbackConverter;
import umc.tickettaka.converter.TicketConverter;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Timeline;
import umc.tickettaka.domain.enums.TicketStatus;
import umc.tickettaka.domain.mapping.TicketReviewer;
import umc.tickettaka.domain.ticket.Feedback;
import umc.tickettaka.domain.ticket.Ticket;
import umc.tickettaka.repository.FeedbackRepository;
import umc.tickettaka.repository.TicketRepository;
import umc.tickettaka.repository.TicketReviewerRepository;
import umc.tickettaka.service.*;
import umc.tickettaka.web.dto.common.CommonMemberDto;
import umc.tickettaka.web.dto.request.TicketRequestDto;
import umc.tickettaka.web.dto.request.TicketRequestDto.CreateTicketDto;
import umc.tickettaka.web.dto.request.TicketRequestDto.DeleteTicketDto;
import umc.tickettaka.web.dto.response.TicketResponseDto;

@Service
@RequiredArgsConstructor
public class TicketCommandServiceImpl implements TicketCommandService {

    private final TimelineQueryService timelineQueryService;
    private final ImageUploadService imageUploadService;
    private final FeedbackRepository feedbackRepository;
    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;
    private final TicketReviewerRepository ticketReviewerRepository;
    private final TicketQueryService ticketQueryService;
    private final TicketRepository ticketRepository;

    @Override
    @Transactional
    public Ticket createTicket(Long timelineId, CreateTicketDto request) {

        Timeline timeline = timelineQueryService.findById(timelineId);
        Member worker = memberQueryService.findByUsername(request.getWorkerName());
        Long sequence = ticketRepository.countByTimeline(timeline) + 1;
        Ticket ticket = TicketConverter.toTicket(timeline, worker, sequence, request);
        Ticket newTicket = ticketRepository.save(ticket);
        setReviewers(request, ticket);

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


    @Override
    @Transactional
    public TicketResponseDto.MemberAchieveLevelDto showMemberAchieve(Member member, Long teamId) {
        CommonMemberDto.ShowMemberProfileDto memberProfileDto = memberCommandService.getMemberProfileDto(member,teamId);

        List<Ticket> memberTickets = ticketQueryService.findAllByWorker(member);
        int total = memberTickets.size();

        List<Ticket> doneTickets = memberTickets.stream()
                .filter(ticket -> ticket.getStatus() == TicketStatus.DONE)
                .toList();
        int done = doneTickets.size();

        return TicketConverter.toMemberAchieveLevelDto(memberProfileDto,total,done);
    }

    @Override
    @Transactional
    public void makeFeedbackRequest(TicketRequestDto.CreateFeedbackDto feedbackDto, List<MultipartFile> files) throws IOException{
        Ticket ticket = ticketQueryService.findById(feedbackDto.getTicketId());
        List<Member> reviewers = feedbackDto.getReviewerList().stream()
                .map(memberQueryService::findByUsername).toList();
        List<String> fileLinks = new ArrayList<>();

        List<TicketReviewer> ticketReviewerList = reviewers.stream()
                .map(reviewer -> TicketReviewer.builder()
                        .member(reviewer)
                        .ticket(ticket)
                        .build()).toList();
        ticketReviewerRepository.saveAll(ticketReviewerList);

        for (MultipartFile file : files) {
            String fileLink = imageUploadService.uploadImage(file);
            fileLinks.add(fileLink);
        }
        Feedback feedback = FeedbackConverter.toFeedback(ticket,fileLinks,feedbackDto.getLinkList());
        feedbackRepository.save(feedback);
    }


    @Override
    @Transactional
    public void acceptFeedback(Long ticketId) {
        Ticket ticket = ticketQueryService.findById(ticketId);
        Feedback feedback = feedbackRepository.findByTicket(ticket);
        List<TicketReviewer> ticketReviewerList = ticketReviewerRepository.findAllByTicket(ticket);

        ticket.updateStatus(TicketStatus.DONE);
        feedbackRepository.delete(feedback);
        ticketReviewerRepository.deleteAll(ticketReviewerList);
    }

    @Override
    @Transactional
    public void rejectFeedback(TicketRequestDto.RejectFeedbackDto reject) {
        Feedback feedback = ticketQueryService.findFeedback(reject.getFeedbackId());
        feedback.reject(reject.getRejectComment());
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
