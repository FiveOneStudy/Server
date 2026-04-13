package fiveonestudy.ddait.study.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyTipListResponse {

    private List<List<Object>> tips;
}