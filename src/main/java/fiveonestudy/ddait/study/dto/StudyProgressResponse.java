package fiveonestudy.ddait.study.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyProgressResponse {

    private int mainProgress;
    private List<List<String>> memberProgress;

    private String name;
    private String progress;

    private List<List<Object>> mission;
}