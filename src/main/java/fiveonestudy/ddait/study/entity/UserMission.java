package fiveonestudy.ddait.study.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_progress_id")
    private UserProgress userProgress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_mission_id")
    private StudyMission studyMission;

    private boolean completed;
}