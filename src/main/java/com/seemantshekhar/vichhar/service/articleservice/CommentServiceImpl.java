package com.seemantshekhar.vichhar.service.articleservice;

import com.seemantshekhar.vichhar.domain.BaseEntity;
import com.seemantshekhar.vichhar.domain.article.ArticleDto;
import com.seemantshekhar.vichhar.domain.article.ArticleEntity;
import com.seemantshekhar.vichhar.domain.article.CommentDto;
import com.seemantshekhar.vichhar.domain.article.CommentEntity;
import com.seemantshekhar.vichhar.domain.user.UserDto;
import com.seemantshekhar.vichhar.domain.user.UserEntity;
import com.seemantshekhar.vichhar.exception.AppException;
import com.seemantshekhar.vichhar.exception.Error;
import com.seemantshekhar.vichhar.repository.ArticleRepository;
import com.seemantshekhar.vichhar.repository.CommentRepository;
import com.seemantshekhar.vichhar.service.profileservice.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private  final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final ProfileService profileService;

    @Transactional
    @Override
    public CommentDto addCommentsToAnArticle(String slug, CommentDto comment, UserDto.Auth authUser) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        CommentEntity commentEntity = CommentEntity.builder()
                .article(articleEntity)
                .body(comment.getBody())
                .author(UserEntity.builder()
                        .id(authUser.getId())
                        .bio(authUser.getBio())
                        .name(authUser.getName())
                        .image(authUser.getImage())
                        .build())
                .build();
        commentRepository.save(commentEntity);
        return convertToCommentDto(commentEntity, false);
    }

    @Transactional
    @Override
    public void delete(String slug, Long commentId, UserDto.Auth authUser) {
        Long articleId = articleRepository.findBySlug(slug).map(BaseEntity::getId)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .filter(comment -> comment.getArticle().getId().equals(articleId))
                .orElseThrow(() -> new AppException(Error.COMMENT_NOT_FOUND));
        commentRepository.delete(commentEntity);
    }

    @Override
    public List<CommentDto> getCommentsBySlug(String slug, UserDto.Auth authUser) {
        Long articleId = articleRepository.findBySlug(slug).map(BaseEntity::getId)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        List<CommentEntity> commentEntities = commentRepository.findByArticleId(articleId);
        return commentEntities.stream().map(commentEntity -> {
            Boolean following = profileService.getProfile(commentEntity.getAuthor().getName(), authUser).getFollowing();
            return convertToCommentDto(commentEntity, following);
        }).collect(Collectors.toList());
    }

    private CommentDto convertToCommentDto(CommentEntity commentEntity, Boolean isFollowing){
        return CommentDto.builder()
                .id(commentEntity.getId())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .body(commentEntity.getBody())
                .author(ArticleDto.Author.builder()
                        .name(commentEntity.getAuthor().getName())
                        .image(commentEntity.getAuthor().getImage())
                        .bio(commentEntity.getAuthor().getBio())
                        .following(isFollowing)
                        .build())
                .build();
    }
}
