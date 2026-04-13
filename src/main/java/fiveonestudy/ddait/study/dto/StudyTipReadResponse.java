package fiveonestudy.ddait.study.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyTipReadResponse {

    private String title;
    private String writer;
    private String date;
    private String content;

    // 🔥 리스트로 변경
    private List<String> url;
}