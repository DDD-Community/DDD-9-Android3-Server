package com.nexters.buyornot.module.post.dao;

import com.nexters.buyornot.module.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByTitle(String title);
}
