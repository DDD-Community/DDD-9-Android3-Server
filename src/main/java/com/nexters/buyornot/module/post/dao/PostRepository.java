package com.nexters.buyornot.module.post.dao;

import com.nexters.buyornot.module.post.dao.querydsl.PostRepositoryCustom;
import com.nexters.buyornot.module.post.domain.model.PollStatus;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.nexters.buyornot.module.post.domain.post.Post;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Post findByTitle(String title);

    @Query("select p from Post p where p.userId = (:user_id) and p.isPublished = (:is_published) order by p.updatedAt DESC")
    List<Post> findTemporaries(UUID user_id, boolean is_published);


    @Query(
            value = "select p from Post p where p.userId = (:user_id) and p.pollStatus IN (:poll_status) order by p.createdAt DESC",
            countQuery = "select count(p) from Post p where p.userId = (:user_id) and p.pollStatus IN (:poll_status) order by p.createdAt DESC"
    )
    Page<Post> findPageByUserAndPollStatus(UUID user_id, PollStatus poll_status, Pageable pageable);

    Page<Post> findPageByIsPublishedAndPublicStatusOrderByIdDesc(boolean isPublished, PublicStatus publicStatus,
                                                                 Pageable pageable);

    List<Post> findByUserIdAndIsPublished(UUID id, boolean isPublished);
}
