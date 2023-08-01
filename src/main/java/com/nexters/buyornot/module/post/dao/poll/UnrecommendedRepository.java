package com.nexters.buyornot.module.post.dao.poll;

import com.nexters.buyornot.module.post.domain.poll.Unrecommended;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnrecommendedRepository extends JpaRepository<Unrecommended, Long> {

    @Query("select u from Unrecommended u where u.post.id = (:postId)")
    List<Unrecommended> findByPostId(Long postId);
}
