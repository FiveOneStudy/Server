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

    private final KoreanRiskAnalyzer analyzer;
    private final ModerationClient client;

    public ModerationResult evaluate(String text) {

        RiskLevel local = analyzer.analyze(text);

        log.info("local risk = {}", local);

        RiskLevel ai = client.analyze(text);

        log.info("ai risk = {}", ai);

        return merge(local, ai);
    }

    private ModerationResult merge(RiskLevel local, RiskLevel ai) {

        if (ai == RiskLevel.HIGH || local == RiskLevel.HIGH) {
            return ModerationResult.BLOCKED;
        }

        if (ai == RiskLevel.MEDIUM) {
            return ModerationResult.REVIEW;
        }

        return ModerationResult.ALLOWED;
    }
}