package com.nexters.buyornot.module.post.domain;

import com.nexters.buyornot.module.model.BaseEntity;
import com.nexters.buyornot.module.model.Price;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PollItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String itemName;

    @Embedded
    private Price itemPrice;

    @Lob
    private String imgUrl;

    private String imgName;

    private String imgType;
}
