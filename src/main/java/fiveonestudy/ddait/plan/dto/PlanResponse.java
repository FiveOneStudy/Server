package fiveonestudy.ddait.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PlanResponse {
    private List<MonthlyPlan> monthPlans;
    private List<String> planList;
    private List<CheckItem> checkList;
}
