package fiveonestudy.ddait.study.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "study_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String studyName;
}