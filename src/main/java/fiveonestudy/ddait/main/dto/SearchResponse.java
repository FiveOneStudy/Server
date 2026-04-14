package fiveonestudy.ddait.main.dto;

import lombok.*;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    private List<String> study;
    private List<List<Object>> tips; // [[id, title], [id, title]] 구조
}