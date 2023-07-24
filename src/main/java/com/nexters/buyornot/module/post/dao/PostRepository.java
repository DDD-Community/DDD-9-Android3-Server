package com.nexters.buyornot.module.post.dao;

import com.nexters.buyornot.module.post.domain.Post;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByTitle(String title);

    Page<Post> findAll(Pageable pageable);

    Page<Post> findPageByPublicStatus(PublicStatus status, Pageable pageable);
}
