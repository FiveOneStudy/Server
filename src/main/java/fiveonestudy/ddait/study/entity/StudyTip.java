package fiveonestudy.ddait.study.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyTip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studyName;

    private String writer;

    private String title;

    private String createdDate;

    @Column(columnDefinition = "TEXT")
    private String content;

    // 🔥 여러 개 URL 저장
    @ElementCollection
    @CollectionTable(
            name = "study_tip_url",
            joinColumns = @JoinColumn(name = "study_tip_id")
    )
    @Column(name = "url")
    private List<String> url;
}