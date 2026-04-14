package fiveonestudy.ddait.study.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MissionCompleteRequest {
    private String studyName;
    private String subject;
}