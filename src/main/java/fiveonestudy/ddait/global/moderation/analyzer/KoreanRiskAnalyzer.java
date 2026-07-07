package fiveonestudy.ddait.global.moderation.analyzer;

import fiveonestudy.ddait.global.moderation.dto.RiskLevel;
import fiveonestudy.ddait.global.moderation.policy.KoreanKeywords;
import fiveonestudy.ddait.global.moderation.policy.KoreanPatterns;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KoreanRiskAnalyzer {

    public RiskLevel analyze(String text) {

        String normalized = normalize(text);

        int score = 0;

        score += matchScore(normalized, KoreanKeywords.VIOLENCE, 3);
        score += matchScore(normalized, KoreanKeywords.SELF_HARM, 4);
        score += matchScore(normalized, KoreanKeywords.SEXUAL, 2);
        score += matchScore(normalized, KoreanKeywords.HARASSMENT, 2);

        if (KoreanPatterns.INTENT.matcher(normalized).find()) {
            score += 2;
        }

        return toRiskLevel(score);
    }

    private int matchScore(String text, List<String> keywords, int weight) {
        return (int) keywords.stream()
                .filter(text::contains)
                .count() * weight;
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