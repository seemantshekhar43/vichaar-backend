package com.seemantshekhar.vichhar.controller.article;

import com.seemantshekhar.vichhar.domain.article.ArticleDto;
import com.seemantshekhar.vichhar.domain.article.CommentDto;
import com.seemantshekhar.vichhar.domain.user.UserDto;
import com.seemantshekhar.vichhar.model.ArticleQueryParam;
import com.seemantshekhar.vichhar.model.FeedParams;
import com.seemantshekhar.vichhar.service.articleservice.ArticleService;
import com.seemantshekhar.vichhar.service.articleservice.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticlesController {
    private final ArticleService articleService;
    private final CommentService commentService;

    @PostMapping
    public ArticleDto.SingleArticle<ArticleDto> createArticle(@Valid @RequestBody ArticleDto.SingleArticle<ArticleDto> article, @AuthenticationPrincipal UserDto.Auth authUser){
        return new ArticleDto.SingleArticle<>(articleService.createArticle(article.getArticle(), authUser));
    }

    @GetMapping("/{slug}")
    public ArticleDto.SingleArticle<ArticleDto> getArticle(@PathVariable String slug, @AuthenticationPrincipal UserDto.Auth authUser){
        return new ArticleDto.SingleArticle<>(articleService.getArticle(slug, authUser));
    }

    @PutMapping("/{slug}")
    public ArticleDto.SingleArticle<ArticleDto> updateArticle(@PathVariable String slug, @Valid @RequestBody ArticleDto.SingleArticle<ArticleDto.Update> article, @AuthenticationPrincipal UserDto.Auth authUser){
        return new ArticleDto.SingleArticle<>(articleService.updateArticle(slug, article.getArticle(), authUser));
    }

    @DeleteMapping("/{slug}")
    public void deleteArticle(@PathVariable String slug, @AuthenticationPrincipal UserDto.Auth authUser){
        articleService.deleteArticle(slug, authUser);
    }

    @PostMapping("/{slug}/favorite")
    public ArticleDto.SingleArticle<ArticleDto> favoriteArticle(@PathVariable String slug, @AuthenticationPrincipal UserDto.Auth authUser){
        return new ArticleDto.SingleArticle<>(articleService.favoriteArticle(slug, authUser));
    }

    @DeleteMapping("/{slug}/favorite")
    public ArticleDto.SingleArticle<ArticleDto> unFavoriteArticle(@PathVariable String slug, @AuthenticationPrincipal UserDto.Auth authUser){
        return new ArticleDto.SingleArticle<>(articleService.unFavoriteArticle(slug, authUser));
    }

    @PostMapping("/{slug}/comments")
    public CommentDto.SingleComment addCommentToAnArticle(@PathVariable String slug, @RequestBody @Valid CommentDto.SingleComment comment, @AuthenticationPrincipal UserDto.Auth authUser){
        return new CommentDto.SingleComment(commentService.addCommentsToAnArticle(slug, comment.getComment(), authUser));
    }

    @DeleteMapping("/{slug}/comments/{commentId}")
    public void deleteComment(@PathVariable("slug") String slug, @PathVariable("commentId") Long commentId, @AuthenticationPrincipal UserDto.Auth authUser) {
        commentService.delete(slug, commentId, authUser);
    }

    @GetMapping("/{slug}/comments")
    public CommentDto.MultipleComments getCommentsFromAnArticle(@PathVariable String slug, @AuthenticationPrincipal UserDto.Auth authUser) {
        return CommentDto.MultipleComments.builder()
                .comments(commentService.getCommentsBySlug(slug, authUser))
                .build();
    }

    @GetMapping("/feed")
    public ArticleDto.MultipleArticle feedArticles(@ModelAttribute @Valid FeedParams feedParams, @AuthenticationPrincipal UserDto.Auth authUser){
        return ArticleDto.MultipleArticle.builder().articles(articleService.feedArticles(authUser, feedParams)).build();
    }

    @GetMapping
    public ArticleDto.MultipleArticle listArticles(@ModelAttribute ArticleQueryParam articleQueryParam, @AuthenticationPrincipal UserDto.Auth authUser){
        return ArticleDto.MultipleArticle.builder().articles(articleService.listArticle(articleQueryParam, authUser)).build();
    }
}

