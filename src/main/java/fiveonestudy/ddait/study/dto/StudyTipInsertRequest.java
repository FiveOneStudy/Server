package fiveonestudy.ddait.study.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyTipInsertRequest {

    private String studyName;
    private String title;
    private String content;
    private String url;
}