package fiveonestudy.ddait.global.moderation.analyzer;

import fiveonestudy.ddait.global.moderation.dto.RiskLevel;
import fiveonestudy.ddait.global.moderation.entity.ModerationKeyword;
import fiveonestudy.ddait.global.moderation.policy.KoreanPatterns;
import fiveonestudy.ddait.global.moderation.provider.KeywordProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KoreanRiskAnalyzer {

    private final KeywordProvider keywordProvider;

    public RiskLevel analyze(String text) {

        String normalized = normalize(text);

        int score = 0;

        for (ModerationKeyword keyword : keywordProvider.getKeywords()) {
            if(normalized.contains(keyword.getKeyword())) {
                score += keyword.getWeight();
            }
        }

        if (KoreanPatterns.INTENT.matcher(normalized).find()) {
            score += 2;
        }

        return toRiskLevel(score);
    }

    private RiskLevel toRiskLevel(int score) {
        if (score >= 6) return RiskLevel.HIGH;
        if (score >= 3) return RiskLevel.MEDIUM;
        return RiskLevel.LOW;
    }

    private String normalize(String text) {
        return text
                .replaceAll("\\s+", "") // 띄어쓰기 제거
                .replaceAll("[^가-힣a-zA-Z0-9]", ""); // 특수문자 제거
    }
}