package fiveonestudy.ddait.study.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studyName;

    private int totalProgress;

    @Column(columnDefinition = "TEXT")
    private String missionList; // JSON 형태로 저장 추천
}