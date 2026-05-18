package fiveonestudy.ddait.study.dto;

import fiveonestudy.ddait.global.xss.NoXss;
import lombok.*;

@NoXss
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MissionCompleteRequest {
    private String studyName;
    private String subject;
}