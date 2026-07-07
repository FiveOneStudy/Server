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

    @Column(unique = true, nullable = false)
    private String keyword;

    private int weight;

    @Enumerated(EnumType.STRING)
    private KeywordCategory category;

    private boolean enabled;
}
