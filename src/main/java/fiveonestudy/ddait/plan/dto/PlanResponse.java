package fiveonestudy.ddait.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponse {
    private List<MonthlyPlan> monthPlans;
    private List<String> planList;
    private List<CheckItem> checkList;
}
