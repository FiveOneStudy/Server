package fiveonestudy.ddait.study.entity;

import jakarta.persistence.*;
import lombok.*;

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

    // 🔥 추가
    private String bookUrl;
}