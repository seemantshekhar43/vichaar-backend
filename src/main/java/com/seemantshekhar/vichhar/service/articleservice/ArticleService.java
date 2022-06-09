package com.seemantshekhar.vichhar.service.articleservice;

import com.seemantshekhar.vichhar.domain.article.ArticleDto;
import com.seemantshekhar.vichhar.domain.user.UserDto;
import com.seemantshekhar.vichhar.model.ArticleQueryParam;
import com.seemantshekhar.vichhar.model.FeedParams;

import java.util.List;

public interface ArticleService {
    ArticleDto createArticle(final ArticleDto article, final UserDto.Auth authUser);

    ArticleDto getArticle(final String slug, final UserDto.Auth authUser);

    ArticleDto updateArticle(final String slug, final ArticleDto.Update article, final UserDto.Auth authUser);

    void deleteArticle(final String slug, final UserDto.Auth authUser);

    List<ArticleDto> feedArticles(final UserDto.Auth authUser, final FeedParams feedParams);

    ArticleDto favoriteArticle(final String slug, final UserDto.Auth authUser);

    ArticleDto unFavoriteArticle(final String slug, final UserDto.Auth authUser);

    List<ArticleDto> listArticle(final ArticleQueryParam articleQueryParam, final UserDto.Auth authUser);


}
