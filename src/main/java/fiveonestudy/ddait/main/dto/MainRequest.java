package fiveonestudy.ddait.main.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainRequest {
    private LocalDate date;
}