package com.nexters.buyornot.module.post.domain.post;

import com.nexters.buyornot.module.item.domain.ItemInfo;
import com.nexters.buyornot.module.model.BaseEntity;
import com.nexters.buyornot.module.model.Price;
import com.nexters.buyornot.module.post.api.dto.response.PollItemResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;


@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "entity_status='ACTIVE'")
public class PollItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private Long itemId;

    private String itemUrl;

    private String brand;

    private String itemName;

    @Embedded
    private Price itemPrice;

    @Lob
    private String imgUrl;

    public static PollItem newPollItem(ItemInfo info) {
        return builder()
                .itemId(info.getId())
                .brand(info.getBrand())
                .itemName(info.getName())
                .itemPrice(info.getPrice())
                .itemUrl(info.getItemUrl())
                .imgUrl(info.getImgUrl())
                .build();
    }

    public void belong(Post post) {
        this.post = post;
    }

    public PollItemResponse newPollItemResponse() {
        return new PollItemResponse(this.id, this.itemId, this.brand, this.itemUrl, this.itemName, this.imgUrl,
                this.itemPrice.getValue(), this.itemPrice.getDiscountRate(), this.itemPrice.getDiscountedPrice());
    }

    public Long getId() {
        return id;
    }
}
