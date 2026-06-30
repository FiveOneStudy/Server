package fiveonestudy.ddait.plan.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CheckListUpdateRequest {
    private Long checkId;
    private String newContent;
}