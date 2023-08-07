package com.nexters.buyornot.module.archive.dao;

import com.nexters.buyornot.module.archive.domain.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {

    @Query("select a from Archive a where a.userId = (:userId) and a.itemUrl = (:url)")
    Optional<Archive> findByUserAndUrl(String userId, String url);

    @Query("select a from Archive a where a.userId = (:userId) and a.itemId = (:itemId)")
    Optional<Archive>findByUserAndItem(String userId, Long itemId);
}