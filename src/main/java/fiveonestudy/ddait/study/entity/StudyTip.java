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

    // String → List<String> 변경, 콤마로 구분해서 하나의 컬럼에 저장
    @ElementCollection
    @CollectionTable(name = "study_tip_urls", joinColumns = @JoinColumn(name = "study_tip_id"))
    @Column(name = "url", length = 1000)
    @Builder.Default
    private List<String> urls = new ArrayList<>();
}