package com.nexters.buyornot.module.post.domain.post;

import com.nexters.buyornot.module.model.BaseEntity;
import com.nexters.buyornot.module.post.api.dto.request.CreatePostReq;
import com.nexters.buyornot.module.post.domain.model.PollStatus;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.nexters.buyornot.module.post.api.dto.response.PollItemResponse;
import com.nexters.buyornot.module.post.api.dto.response.PostResponse;
import com.nexters.buyornot.module.user.dto.JwtUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "entity_status='ACTIVE'")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @NotNull(message = "로그인해주세요.")
    private UUID userId;

    private String nickname;

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

    private Post(JwtUser user, CreatePostReq dto, List<PollItem> pollItems) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.publicStatus = dto.getPublicStatus();
        this.pollItems = pollItems;

        for(PollItem pollItem : pollItems) {
            pollItem.belong(this);
        }
    }

    public static Post newPost(JwtUser jwtUser, CreatePostReq dto, List<PollItem> pollItems) {
        return new Post(jwtUser, dto, pollItems);
    }

    public PostResponse newPostResponse() {
        List<PollItemResponse> pollItemResponseList = new ArrayList<>();
        for(PollItem pollItem : pollItems) {
            PollItemResponse response = pollItem.newPollItemResponse();
            pollItemResponseList.add(response);
        }
        return new PostResponse(id, userId.toString(), nickname, title, content, publicStatus.name(), pollStatus.name(), pollItemResponseList);
    }

    public Long getId() {
        return id;
    }

    public boolean checkValidity(UUID userId) {
        if(this.userId.equals(userId)) return true;
        return false;
    }

    public void update(CreatePostReq dto, List<PollItem> pollItems) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.publicStatus = dto.getPublicStatus();
        this.pollItems = pollItems;
    }

    public List<Long> getItemList() {
        List<Long> itemList = new ArrayList<>();
        for(PollItem pollItem : pollItems) itemList.add(pollItem.getId());
        return itemList;
    }

    public PollItem getPollItem(Long itemId) {
        PollItem pollItem = null;
        for(PollItem item : pollItems) {
            if(item.getId().equals(itemId)) pollItem = item;
        }

        return pollItem;
    }
}
