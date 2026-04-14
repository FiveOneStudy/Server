package fiveonestudy.ddait.main.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckCompleteRequest {
    private LocalDate date;
    private String content;
}