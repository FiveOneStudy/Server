package fiveonestudy.ddait.study.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String missionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_progress_id")
    private StudyProgress studyProgress;
}