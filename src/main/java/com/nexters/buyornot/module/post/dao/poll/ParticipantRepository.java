package com.nexters.buyornot.module.post.dao.poll;

import com.nexters.buyornot.module.post.domain.poll.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query("select p from Participant p where p.pollItem.id = (:itemId)")
    List<Participant> findByPollItemId(Long itemId);
}
