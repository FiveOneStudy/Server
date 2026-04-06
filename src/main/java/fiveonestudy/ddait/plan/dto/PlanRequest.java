package fiveonestudy.ddait.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PlanRequest {
    private String email;
    private LocalDate date;
}
