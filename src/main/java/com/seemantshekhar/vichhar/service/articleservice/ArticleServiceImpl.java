package com.seemantshekhar.vichhar.service.articleservice;

import com.seemantshekhar.vichhar.domain.BaseEntity;
import com.seemantshekhar.vichhar.domain.article.ArticleDto;
import com.seemantshekhar.vichhar.domain.article.ArticleEntity;
import com.seemantshekhar.vichhar.domain.article.FavoriteEntity;
import com.seemantshekhar.vichhar.domain.profile.FollowEntity;
import com.seemantshekhar.vichhar.domain.tag.ArticleTagRelationEntity;
import com.seemantshekhar.vichhar.domain.user.UserDto;
import com.seemantshekhar.vichhar.domain.user.UserEntity;
import com.seemantshekhar.vichhar.exception.AppException;
import com.seemantshekhar.vichhar.exception.Error;
import com.seemantshekhar.vichhar.model.ArticleQueryParam;
import com.seemantshekhar.vichhar.model.FeedParams;
import com.seemantshekhar.vichhar.repository.ArticleRepository;
import com.seemantshekhar.vichhar.repository.FavoriteRepository;
import com.seemantshekhar.vichhar.repository.FollowRepository;
import com.seemantshekhar.vichhar.service.profileservice.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{
    private final ArticleRepository articleRepository;
    private final FollowRepository followRepository;
    private final FavoriteRepository favoriteRepository;

    private  final ProfileService profileService;


    @Transactional
    @Override
    public ArticleDto createArticle(ArticleDto article, UserDto.Auth authUser) {
        String slug = String.join("-", article.getTitle().split("\\s+"));
        UserEntity author = UserEntity.builder()
                .id(authUser.getId())
                .name(authUser.getName())
                .bio(authUser.getBio())
                .image(authUser.getImage())
                .build();
        ArticleEntity articleEntity = ArticleEntity.builder()
                .slug(slug)
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .author(author)
                .build();
        List<ArticleTagRelationEntity> tagList = new ArrayList<>();
        for(String tag: article.getTagList()){
            tagList.add(ArticleTagRelationEntity.builder()
                    .article(articleEntity)
                    .tag(tag)
                    .build());
        }
        articleEntity.setTagList(tagList);
        articleEntity = articleRepository.save(articleEntity);

        return convertToArticleDto(articleEntity, false, 0L, false);
    }


    @Override
    public ArticleDto getArticle(String slug, UserDto.Auth authUser) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        Boolean following = profileService.getProfile(articleEntity.getAuthor().getName(), authUser).getFollowing();
        List<FavoriteEntity> favorites = articleEntity.getFavoriteList();
        Boolean favorited = favorites.stream().anyMatch(favoriteEntity -> favoriteEntity.getUser().getId().equals(authUser.getId()));
        long favoriteCount = favorites.size();
        return convertToArticleDto(articleEntity, favorited, favoriteCount, following);
    }

    @Transactional
    @Override
    public ArticleDto updateArticle(String slug, ArticleDto.Update article, UserDto.Auth authUser) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));

        if(article.getTitle() != null){
            String newSlug = String.join("-", article.getTitle().split("\\s+"));
            articleEntity.setSlug(newSlug);
            articleEntity.setTitle(article.getTitle());
        }

        if(article.getDescription() != null){
            articleEntity.setDescription(article.getDescription());
        }

        if(article.getBody() != null){
            articleEntity.setBody(article.getBody());
        }

        articleRepository.save(articleEntity);
        return getArticle(articleEntity.getSlug(), authUser);
    }

    @Override
    public void deleteArticle(String slug, UserDto.Auth authUser) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        articleRepository.delete(articleEntity);
    }

    @Override
    public List<ArticleDto> feedArticles(UserDto.Auth authUser, FeedParams feedParams) {
        List<Long> feedAuthorIds = followRepository.findByFollowerId(authUser.getId()).stream()
                .map(FollowEntity::getFollowee).map(BaseEntity::getId).collect(Collectors.toList());
        return articleRepository.findByAuthorIdInOrderByCreatedAtDesc(feedAuthorIds, PageRequest.of(feedParams.getOffset(), feedParams.getLimit()))
                .stream().map(entity -> {
                    List<FavoriteEntity> favorites = entity.getFavoriteList();
                    Boolean favorited = favorites.stream().anyMatch(favoriteEntity -> favoriteEntity.getUser().getId().equals(authUser.getId()));
                    long favoritesCount = favorites.size();
                    return convertToArticleDto(entity, favorited, favoritesCount, true);
                }).collect(Collectors.toList());
    }

    @Override
    public ArticleDto favoriteArticle(String slug, UserDto.Auth authUser) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        favoriteRepository.findByArticleIdAndUserId(articleEntity.getId(), authUser.getId())
                .ifPresent(favoriteEntity -> {throw  new AppException(Error.ALREADY_FAVORITED_ARTICLE);});
        FavoriteEntity favoriteEntity = FavoriteEntity.builder()
                .article(articleEntity)
                .user(UserEntity.builder()
                        .id(authUser.getId())
                        .build())
                .build();
        favoriteRepository.save(favoriteEntity);
        return getArticle(slug, authUser);
    }

    @Override
    public ArticleDto unFavoriteArticle(String slug, UserDto.Auth authUser) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        FavoriteEntity favorite = articleEntity.getFavoriteList().stream()
                .filter(favoriteEntity -> favoriteEntity.getArticle().getId().equals(articleEntity.getId())
                && favoriteEntity.getUser().getId().equals(authUser.getId())).findAny()
                .orElseThrow(() -> new AppException(Error.FAVORITE_NOT_FOUND));

        articleEntity.getFavoriteList().remove(favorite);
        return getArticle(slug, authUser);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ArticleDto> listArticle(ArticleQueryParam articleQueryParam, UserDto.Auth authUser) {
        Pageable pageable = null;
        if(articleQueryParam.getOffset() != null){
            pageable = PageRequest.of(articleQueryParam.getOffset(), articleQueryParam.getLimit());
        }

        List<ArticleEntity> articleEntities;
        if(articleQueryParam.getTag() != null){
            articleEntities = articleRepository.findByTag(articleQueryParam.getTag(), pageable);
        }else if  (articleQueryParam.getAuthor() != null) {
            articleEntities = articleRepository.findByAuthorName(articleQueryParam.getAuthor(), pageable);
        } else if (articleQueryParam.getFavorited() != null) {
            articleEntities = articleRepository.findByFavoritedUsername(articleQueryParam.getFavorited(), pageable);
        } else {
            articleEntities = articleRepository.findListByPaging(pageable);
        }

        return convertToArticleList(articleEntities, authUser);

    }

    private ArticleDto convertToArticleDto(ArticleEntity article, Boolean favorited, Long favoritesCount, Boolean following){
        return ArticleDto.builder()
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .author(ArticleDto.Author.builder()
                        .name(article.getAuthor().getName())
                        .bio(article.getAuthor().getBio())
                        .image(article.getAuthor().getImage())
                        .following(following)
                        .build())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .favorited(favorited)
                .favoritesCount(favoritesCount)
                .tagList(article.getTagList().stream().map(ArticleTagRelationEntity::getTag).collect(Collectors.toList()))
                .build();
    }

    private List<ArticleDto> convertToArticleList(List<ArticleEntity> articleEntities, UserDto.Auth authUser) {
        List<Long> authorIds = articleEntities.stream().map(ArticleEntity::getAuthor).map(BaseEntity::getId).collect(Collectors.toList());
        List<Long> followeeIds = followRepository.findByFollowerIdAndFolloweeIdIn(authUser.getId(), authorIds).stream().map(FollowEntity::getFollowee).map(BaseEntity::getId).collect(Collectors.toList());

        return articleEntities.stream().map(entity -> {
            List<FavoriteEntity> favorites = entity.getFavoriteList();
            Boolean favorited = favorites.stream().anyMatch(favoriteEntity -> favoriteEntity.getUser().getId().equals(authUser.getId()));
            int favoriteCount = favorites.size();
            Boolean following = followeeIds.stream().anyMatch(followeeId -> followeeId.equals(entity.getAuthor().getId()));
            return convertToArticleDto(entity, favorited, (long) favoriteCount, following);
        }).collect(Collectors.toList());
    }
}
