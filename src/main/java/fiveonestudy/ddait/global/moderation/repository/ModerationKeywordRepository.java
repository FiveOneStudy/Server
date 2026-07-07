package fiveonestudy.ddait.global.moderation.repository;

import fiveonestudy.ddait.global.moderation.entity.ModerationKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModerationKeywordRepository
        extends JpaRepository<ModerationKeyword, Long> {

    List<ModerationKeyword> findByEnabledTrue();
}
