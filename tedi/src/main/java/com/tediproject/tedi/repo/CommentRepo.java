package com.tediproject.tedi.repo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tediproject.tedi.model.Article;
import com.tediproject.tedi.model.Comments;
import com.tediproject.tedi.model.UserEntity;



public interface CommentRepo extends JpaRepository<Comments, Long>{
    long countByArticle(Article article);
        
    @Query("SELECT c.poster FROM Comments c WHERE c.article = ?1")
    List <UserEntity> findUserEntityByArticle(Article article);

    @Query("SELECT c FROM Comments c WHERE c.article = ?1")
    List <Comments> findCommentsByArticle(Article article);
}
