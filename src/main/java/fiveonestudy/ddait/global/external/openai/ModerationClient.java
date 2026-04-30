package fiveonestudy.ddait.global.external.openai;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.moderations.Moderation;
import com.openai.models.moderations.ModerationCreateParams;
import fiveonestudy.ddait.global.external.openai.entity.ModerationPolicy;
import fiveonestudy.ddait.global.moderation.dto.RiskLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ModerationClient {

    private final OpenAIClient client;

    public ModerationClient(@Value("${openai.api.key}") String apiKey) {
        this.client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();
    }

    public RiskLevel analyze(String input) {

        if (input == null || input.isBlank()) {
            return RiskLevel.LOW;
        }

        try {
            ModerationCreateParams params = ModerationCreateParams.builder()
                    .model("omni-moderation-latest")
                    .input(input)
                    .build();

            log.info("🚀 OpenAI 호출 직전");
            log.info("model = omni-moderation-latest, input length = {}", input.length());

            var response = client.moderations().create(params);

            var scores = response.results().get(0).categoryScores();

            double v = scores.violence();
            double s = scores.sexual();
            double sh = scores.selfHarm();
            double h = scores.harassment();

            RiskLevel level = toRiskLevel(scores);

            log.info("MODERATION → level={}, v={}, s={}, sh={}, h={}",
                    level, v, s, sh, h);

            return level;

        } catch (Exception e) {
            log.error("moderation 실패", e);
            throw new RuntimeException("MODERATION_API_FAILED");
        }
    }


    private RiskLevel toRiskLevel(
            Moderation.CategoryScores scores
    ) {

        double violence = scores.violence();
        double sexual = scores.sexual();
        double selfHarm = scores.selfHarm();
        double harassment = scores.harassment();

        if (violence > ModerationPolicy.VIOLENCE_THRESHOLD
                || sexual > ModerationPolicy.SEXUAL_THRESHOLD
                || selfHarm > ModerationPolicy.SELF_HARM_THRESHOLD
                || harassment > ModerationPolicy.HARASSMENT_THRESHOLD) {
            return RiskLevel.HIGH;
        }

        double max = Math.max(
                Math.max(violence, sexual),
                Math.max(selfHarm, harassment)
        );

        if (max >= 0.5) return RiskLevel.MEDIUM;

        return RiskLevel.LOW;
    }
}