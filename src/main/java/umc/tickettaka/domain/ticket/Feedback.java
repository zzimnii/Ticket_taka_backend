package umc.tickettaka.domain.ticket;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.Cascade;
import umc.tickettaka.domain.common.BaseEntity;
import umc.tickettaka.domain.enums.FeedbackStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Feedback extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500)
    private String body;
    @Enumerated(EnumType.STRING)
    private FeedbackStatus status;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> linkList = new ArrayList<>();
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> fileList = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public Feedback reject(String rejctComment){
        this.body=rejctComment;
        this.status = FeedbackStatus.REJECT;
        return this;
    }
}
