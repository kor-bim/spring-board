package com.bim93.springboard.repository;

import com.bim93.springboard.config.JpaConfig;
import com.bim93.springboard.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
@DisplayName("JPA Connection Test")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository, @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("Insert Test")
    @Test
    void insertTest() {
        long previousCount = articleRepository.count();
        articleRepository.save(Article.of("new article", "new content", "hamburger"));
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("Select Test")
    @Test
    void selectTest() {
        List<Article> articles = articleRepository.findAll();
        assertThat(articles).isNotNull().hasSize(0);
    }

    @DisplayName("Update Test")
    @Test
    void updateTest() {
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashTag = "#springBoot";
        article.setHashtag(updatedHashTag);

        Article savedArticle = articleRepository.saveAndFlush(article);

        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashTag);
    }

    @DisplayName("Delete Test")
    @Test
    void deleteTest() {
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousCount = articleRepository.count();
        long previousCommentCount = articleCommentRepository.count();
        int deletedCommentSize = article.getArticleComments().size();

        articleRepository.delete(article);

        assertThat(articleRepository.count()).isEqualTo(previousCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousCommentCount - deletedCommentSize);
    }
}