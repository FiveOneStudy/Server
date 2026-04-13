package fiveonestudy.ddait.study.dto;

import lombok.*;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyTipReadResponse {

    private String title;
    private String writer;
    private LocalDate date;
    private String content;
    private String url;
}