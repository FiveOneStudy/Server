package fiveonestudy.ddait.study.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyResponse {

    private List<MyStudy> study;
    private List<String> allStudy;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyStudy {
        private String name;
        private int dday;
    }
}