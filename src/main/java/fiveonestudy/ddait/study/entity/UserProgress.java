package fiveonestudy.ddait.study.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_progress_id")
    private StudyProgress studyProgress;

    @OneToMany(mappedBy = "userProgress", cascade = CascadeType.ALL)
    private List<UserMission> userMissions;
}