package fiveonestudy.ddait.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class MonthlyPlan {

    private LocalDate date;
    private List<String> plans;

}