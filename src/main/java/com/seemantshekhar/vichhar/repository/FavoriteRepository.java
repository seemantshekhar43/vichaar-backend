package com.seemantshekhar.vichhar.repository;

import com.seemantshekhar.vichhar.domain.article.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    Optional<FavoriteEntity> findByArticleIdAndUserId(Long articleId, Long userId);
}
