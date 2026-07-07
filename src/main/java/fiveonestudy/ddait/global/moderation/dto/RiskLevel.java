package fiveonestudy.ddait.global.moderation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RiskLevel {
    LOW(1),
    MEDIUM(2),
    HIGH(4);

    private final int score;

    public boolean isAtLeast(RiskLevel other) {
        return this.score >= other.score;
    }
}