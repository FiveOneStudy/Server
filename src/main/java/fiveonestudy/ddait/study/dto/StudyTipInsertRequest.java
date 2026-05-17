package fiveonestudy.ddait.study.dto;

import fiveonestudy.ddait.global.xss.NoXss;
import lombok.*;

@NoXss
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