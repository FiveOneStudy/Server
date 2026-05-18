package fiveonestudy.ddait.study.dto;

import fiveonestudy.ddait.global.xss.NoXss;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@NoXss
@Getter
@Setter
@NoArgsConstructor
public class StudyTipInsertRequest {

    private String studyName;
    private String title;
    private String content;
    private List<String> url = new ArrayList<>();
}