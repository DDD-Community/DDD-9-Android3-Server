package com.nexters.buyornot.module.item.dao;

import com.nexters.buyornot.module.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByItemUrl(String url);
}
