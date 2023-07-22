package com.nexters.buyornot.module.post.domain;

import com.nexters.buyornot.module.model.BaseEntity;
import com.nexters.buyornot.module.post.dto.request.CreatePostReq;
import com.nexters.buyornot.module.post.domain.model.PollStatus;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.nexters.buyornot.module.post.dto.response.PollItemResponse;
import com.nexters.buyornot.module.post.dto.response.PostResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @NotNull(message = "로그인해주세요.")
    private UUID userId;

    @NotNull(message = "제목을 입력해주세요.")
    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private PublicStatus publicStatus = PublicStatus.PUBLIC;

    @Enumerated(EnumType.STRING)
    private PollStatus pollStatus = PollStatus.ONGOING;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PollItem> pollItems = new ArrayList<>();

    public Post(UUID userId, CreatePostReq dto, List<PollItem> pollItems) {
        this.userId = userId;
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.publicStatus = dto.getPublicStatus();
        this.pollItems = pollItems;

        for(PollItem pollItem : pollItems) {
            pollItem.belong(this);
        }
    }

    public static Post newPost(UUID userId, CreatePostReq dto, List<PollItem> pollItems) {
        return new Post(userId, dto, pollItems);
    }

    public PostResponse newPostResponse() {
        List<PollItemResponse> pollItemResponseList = new ArrayList<>();
        for(PollItem pollItem : pollItems) {
            PollItemResponse response = pollItem.newPollItemResponse();
            pollItemResponseList.add(response);
        }
        return new PostResponse(this.id, this.title, this.content, this.publicStatus.name(), this.pollStatus.name(), pollItemResponseList);
    }

    public Long getId() {
        return id;
    }
}
