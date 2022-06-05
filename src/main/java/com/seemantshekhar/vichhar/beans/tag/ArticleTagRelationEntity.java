package com.seemantshekhar.vichhar.beans.tag;

import com.seemantshekhar.vichhar.beans.BaseEntity;
import com.seemantshekhar.vichhar.beans.article.ArticleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
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
