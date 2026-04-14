package fiveonestudy.ddait.study.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionSearchResponse {
    private List<List<Object>> mission;
}