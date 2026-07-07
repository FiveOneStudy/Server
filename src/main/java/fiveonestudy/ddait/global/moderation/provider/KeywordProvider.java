package fiveonestudy.ddait.global.moderation.provider;

import fiveonestudy.ddait.global.moderation.entity.ModerationKeyword;
import fiveonestudy.ddait.global.moderation.repository.ModerationKeywordRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordProvider {

    private final ModerationKeywordRepository keywordRepository;

    private List<ModerationKeyword> keywords;

    @PostConstruct
    public void load() {
        keywords = keywordRepository.findByEnabledTrue();
    }

    public List<ModerationKeyword> getKeywords() {
        return keywords;
    }

    public void refresh() {
        keywords = keywordRepository.findByEnabledTrue();
    }
}
