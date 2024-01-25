package umc.tickettaka.domain;

import jakarta.persistence.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.tickettaka.domain.common.BaseEntity;
import umc.tickettaka.domain.mapping.MemberTeam;
import umc.tickettaka.domain.mapping.ScheduleTeam;
import umc.tickettaka.web.dto.request.TeamRequestDto;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Team extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String name;
    @Column(length = 500)
    private String imageUrl;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberTeam> memberTeamList = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projectList = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleTeam> scheduleTeamList = new ArrayList<>();

    public Team update(String imageUrl, TeamRequestDto.CreateTeamDto teamUpdateDto) {
        this.imageUrl = imageUrl;

        String updateName = teamUpdateDto.getTeamName();
        if(updateName != null)
            this.name = updateName;

        return this;
    }
}
