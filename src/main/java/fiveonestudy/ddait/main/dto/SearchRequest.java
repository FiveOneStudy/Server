package fiveonestudy.ddait.main.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest {
    private String search;
    private String select; // "study" 또는 "community"
}