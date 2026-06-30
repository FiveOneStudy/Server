package fiveonestudy.ddait.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckItem {
    private Long checkId;
    private String checkContent;
    private boolean completed;
}