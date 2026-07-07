package fiveonestudy.ddait.study.entity;

import fiveonestudy.ddait.user.entity.User;
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

    private String nickname; // 표시용으로 유지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 실제 식별 기준

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_progress_id")
    private StudyProgress studyProgress;

    @OneToMany(mappedBy = "userProgress", cascade = CascadeType.ALL)
    private List<UserMission> userMissions;
}