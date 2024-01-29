package umc.tickettaka.domain.ticket;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Timeline;
import umc.tickettaka.domain.common.BaseEntity;
import umc.tickettaka.domain.enums.TicketStatus;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Ticket extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sequence;
    @Column(length = 50)
    private String title;
    @Column(length = 500)
    private String description;
    private LocalDate startTime;
    private LocalDate endTime;
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeline_id")
    private Timeline timeline;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Member worker;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_ticket_id")
    private Ticket nextTicket;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> fileList = new ArrayList<>();


    public Ticket updateStatus(TicketStatus updatedStatus)
    {
        this.status = updatedStatus;
        return this;
    }
}
