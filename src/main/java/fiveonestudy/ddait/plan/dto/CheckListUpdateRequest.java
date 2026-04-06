package fiveonestudy.ddait.plan.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CheckListUpdateRequest {

    private LocalDate date;
    private String oldContent;
    private String newContent;
}