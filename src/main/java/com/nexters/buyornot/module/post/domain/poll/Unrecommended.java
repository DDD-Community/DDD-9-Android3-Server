package com.nexters.buyornot.module.post.domain.poll;

import com.nexters.buyornot.module.model.BaseEntity;
import com.nexters.buyornot.module.post.api.dto.request.CreatePostReq;
import com.nexters.buyornot.module.post.domain.post.PollItem;
import com.nexters.buyornot.module.post.domain.post.Post;
import com.nexters.buyornot.module.user.dto.JwtUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Unrecommended extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_unrecommended_id")
    private Long id;

    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Unrecommended(Post post, String userId) {
        this.userId = userId;
        this.post = post;
    }

    public static Unrecommended newPoll(Post post, String userId) {
        return new Unrecommended(post, userId);
    }

    public Long getPostId() {
        return post.getId();
    }

    public String getUserId() {
        return userId;
    }
}
