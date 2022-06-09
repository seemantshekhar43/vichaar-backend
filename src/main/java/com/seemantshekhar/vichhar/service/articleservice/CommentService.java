package com.seemantshekhar.vichhar.service.articleservice;

import com.seemantshekhar.vichhar.domain.article.CommentDto;
import com.seemantshekhar.vichhar.domain.user.UserDto;

import java.util.List;

public interface CommentService {
    CommentDto addCommentsToAnArticle(final String slug, final CommentDto comment, final UserDto.Auth authUser);

    void delete(final String slug, final Long commentId, final UserDto.Auth authUser);

    List<CommentDto> getCommentsBySlug(final String slug, final UserDto.Auth authUser);
}
