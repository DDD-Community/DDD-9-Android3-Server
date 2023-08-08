package com.nexters.buyornot.module.post.dao;

import com.nexters.buyornot.module.post.domain.model.PollStatus;
import com.nexters.buyornot.module.post.domain.poll.Participant;
import com.nexters.buyornot.module.post.domain.post.Post;
import com.nexters.buyornot.module.post.domain.poll.Unrecommended;
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

    @Query("select p from Post p join fetch p.pollItems")
    List<Post> findAllFetchJoin();

    Page<Post> findPageByPublicStatusOrderByIdDesc(PublicStatus status, Pageable pageable);

    @Query("select p from Post p where p.userId = (:user_id) and p.publicStatus IN (:public_status) order by p.updatedAt DESC")
    List<Post> findTemporaries(UUID user_id, PublicStatus public_status);


    @Query(
            value = "select p from Post p where p.userId = (:user_id) and p.pollStatus IN (:poll_status) order by p.createdAt DESC",
            countQuery = "select count(p) from Post p where p.userId = (:user_id) and p.pollStatus IN (:poll_status) order by p.createdAt DESC"
    )
    Page<Post> findPageByUserAndPollStatus(UUID user_id, PollStatus poll_status, Pageable pageable);

    List<Post> findByUserIdAndPublicStatus(UUID userId, PublicStatus publicStatus);
}
