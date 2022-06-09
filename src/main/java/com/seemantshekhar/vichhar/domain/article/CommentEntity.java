package com.seemantshekhar.vichhar.domain.article;

import com.seemantshekhar.vichhar.domain.BaseEntity;
import com.seemantshekhar.vichhar.domain.user.UserEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
@NamedEntityGraph(name = "fetch-author", attributeNodes = @NamedAttributeNode("author"))
public class CommentEntity extends BaseEntity {
    @Column(nullable = false)
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private ArticleEntity article;
}
