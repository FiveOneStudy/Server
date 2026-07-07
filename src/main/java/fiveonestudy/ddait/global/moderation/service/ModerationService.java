package fiveonestudy.ddait.global.moderation.service;

import fiveonestudy.ddait.global.external.openai.ModerationClient;
import fiveonestudy.ddait.global.moderation.analyzer.KoreanRiskAnalyzer;
import fiveonestudy.ddait.global.moderation.dto.ModerationResult;
import fiveonestudy.ddait.global.moderation.dto.RiskLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModerationService {

    private final KoreanRiskAnalyzer koreanRiskAnalyzer;
    private final ModerationClient moderationClient;

    public ModerationResult evaluate(String text) {

        RiskLevel localRisk = koreanRiskAnalyzer.analyze(text);
        log.info("Local risk: {}", localRisk);

        RiskLevel aiRisk = moderationClient.analyze(text);
        log.info("AI risk: {}", aiRisk);

        return merge(localRisk, aiRisk);
    }

    private ModerationResult merge(RiskLevel local, RiskLevel ai) {

        if (local == RiskLevel.HIGH || ai == RiskLevel.HIGH) {
            return ModerationResult.BLOCKED;
        }

        if (local == RiskLevel.MEDIUM || ai == RiskLevel.MEDIUM) {
            return ModerationResult.REVIEW;
        }

        return ModerationResult.ALLOWED;
    }
}