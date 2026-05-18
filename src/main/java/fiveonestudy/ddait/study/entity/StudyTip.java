package fiveonestudy.ddait.study.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @Column(length = 1000)
    private String title;

    private LocalDate createdDate;

    @Column(length = 1000)
    private String content;

    @ElementCollection
    @CollectionTable(name = "study_tip_urls", joinColumns = @JoinColumn(name = "study_tip_id"))
    @Column(name = "url", length = 1000)
    @Builder.Default
    private List<String> url = new ArrayList<>();
}