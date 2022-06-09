package com.seemantshekhar.vichhar.repository;

import com.seemantshekhar.vichhar.domain.tag.ArticleTagRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<ArticleTagRelationEntity, Long> {
}
