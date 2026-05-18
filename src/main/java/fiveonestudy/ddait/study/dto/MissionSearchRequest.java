package fiveonestudy.ddait.study.dto;

import fiveonestudy.ddait.global.xss.NoXss;
import lombok.*;

@NoXss
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MissionSearchRequest {
    private String studyName;
    private String search;
}