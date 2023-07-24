package com.nexters.buyornot.module.archive.domain;

import com.nexters.buyornot.module.model.BaseEntity;
import com.nexters.buyornot.module.model.Price;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Archive extends BaseEntity {

    @Id
    @Column(name = "archive_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID userId;

    private Long itemId;

    private String brand;

    private String itemName;

    @Embedded
    private Price price;

    @Lob
    private String imgUrl;
}
