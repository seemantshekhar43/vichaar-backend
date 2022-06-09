package com.seemantshekhar.vichhar.domain.tag;

import com.seemantshekhar.vichhar.domain.BaseEntity;
import com.seemantshekhar.vichhar.domain.article.ArticleEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tags")
public class ArticleTagRelationEntity extends BaseEntity {
    @Column(name="tag", nullable = false)
    private String tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="article", nullable = false)
    private ArticleEntity article;
}
