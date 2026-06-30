package fiveonestudy.ddait.main.dto;

import lombok.*;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunitySearchResponse {
    private List<List<Object>> post; // [[postId, viewCount, title]]
}