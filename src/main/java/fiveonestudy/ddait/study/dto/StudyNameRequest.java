package fiveonestudy.ddait.study.dto;

import fiveonestudy.ddait.global.xss.NoXss;
import lombok.*;

@NoXss
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyNameRequest {
    private String studyName;
}