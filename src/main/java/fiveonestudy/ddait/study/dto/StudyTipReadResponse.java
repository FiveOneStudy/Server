package fiveonestudy.ddait.study.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    // String → List<String> 변경
    @Builder.Default
    private List<String> urls = new ArrayList<>();
    private boolean button;
}