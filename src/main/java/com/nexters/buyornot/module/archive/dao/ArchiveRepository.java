package com.nexters.buyornot.module.archive.dao;

import com.nexters.buyornot.module.archive.domain.Archive;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {

    @Query("select a from Archive a where a.userId = (:userId) and a.itemUrl = (:url)")
    Optional<Archive> findByUserAndUrl(@Param("userId") String userId,@Param("url") String url);

    @Query("select a from Archive a where a.userId = (:userId) and a.itemId = (:itemId)")
    Optional<Archive> findByUserAndItem(@Param("userId") String userId,@Param("itemId)") Long itemId);

    List<Archive> findPageByUserIdOrderByUpdatedAtDesc(String userId, Pageable pageable);

    List<Archive> findPageByUserIdAndIsLikedOrderByUpdatedAtDesc(String userId, boolean isLiked, PageRequest of);

    Optional<Archive> findByIdAndUserId(Long archiveId, String userId);

    void deleteAllByUserId(String userId);
}
