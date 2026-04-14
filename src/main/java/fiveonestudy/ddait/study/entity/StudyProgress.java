package fiveonestudy.ddait.study.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studyName;

    @OneToMany(mappedBy = "studyProgress", cascade = CascadeType.ALL)
    private List<StudyMission> missions;

    @OneToMany(mappedBy = "studyProgress", cascade = CascadeType.ALL)
    private List<UserProgress> userProgressList;
}