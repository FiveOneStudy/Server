package fiveonestudy.ddait.global.moderation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ModerationKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String keyword;

    private int weight;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private KeywordCategory category;

    private boolean enabled;
}
