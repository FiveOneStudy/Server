package fiveonestudy.ddait.main.dto;

import fiveonestudy.ddait.plan.dto.CheckItem;
import fiveonestudy.ddait.plan.dto.MonthlyPlan;
import fiveonestudy.ddait.study.dto.StudyResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainResponse {
    private List<StudyResponse.MyStudy> study;
    private List<MonthlyPlan> monthPlans;
    private List<String> planList;
    private List<CheckItem> checkList;
    private List<String> certification;
}