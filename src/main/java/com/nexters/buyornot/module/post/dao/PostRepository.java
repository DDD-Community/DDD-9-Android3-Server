package com.nexters.buyornot.module.post.dao;

import com.nexters.buyornot.module.post.domain.Post;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByTitle(String title);

    Page<Post> findAll(Pageable pageable);

    Page<Post> findPageByPublicStatusOrderByIdDesc(PublicStatus status, Pageable pageable);

    @Query("select p from Post p where p.userId = (:user_id) and p.publicStatus IN (:public_status) order by p.updatedAt DESC")
    List<Post> findTemporaries(UUID user_id, PublicStatus public_status);


    @Query(
            value = "select * from post where user_id = (:user_id) and public_status IN (:public_status) order by created_at DESC",
            countQuery = "select * from post",
            nativeQuery = true
    )
    Page<Post> findPageByUser(UUID user_id, String public_status, Pageable pageable);
}
