package fiveonestudy.ddait.study.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

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

    @Column(length = 1000)
    private String title;

    private LocalDate createdDate;

    @Column(length = 1000)
    private String content;

    @Column(length = 1000)
    private String url;
}