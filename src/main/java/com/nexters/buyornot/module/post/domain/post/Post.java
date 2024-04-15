package com.nexters.buyornot.module.post.domain.post;

import com.nexters.buyornot.module.model.BaseEntity;
import com.nexters.buyornot.module.post.api.dto.request.CreatePostReq;
import com.nexters.buyornot.module.post.api.dto.request.FromArchive;
import com.nexters.buyornot.module.post.api.dto.response.PollItemResponse;
import com.nexters.buyornot.module.post.api.dto.response.PostResponse;
import com.nexters.buyornot.module.post.domain.model.PollStatus;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.nexters.buyornot.module.user.api.dto.JwtUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;


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
    private String profile;

    @NotNull(message = "제목을 입력해주세요.")
    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private PublicStatus publicStatus;

    @ColumnDefault("true")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean isPublished;

    @Enumerated(EnumType.STRING)
    private PollStatus pollStatus;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PollItem> pollItems;

    private Post(JwtUser user, CreatePostReq dto, List<PollItem> pollItems) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.profile = user.getProfile();
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.publicStatus = Objects.nonNull(dto.getPublicStatus()) ? dto.getPublicStatus() : PublicStatus.PUBLIC;
        this.isPublished = dto.isPublished();
        this.pollItems = Objects.nonNull(pollItems) ? pollItems : List.of();
        if (!isPublished) {
            this.pollStatus = PollStatus.TEMPORARY;
        } else {
            this.pollStatus = PollStatus.ONGOING;
        }

        for (PollItem pollItem : pollItems) {
            pollItem.belong(this);
        }
    }

    private Post(JwtUser user, FromArchive dto, List<PollItem> pollItems) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.profile = user.getProfile();
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.publicStatus = Objects.nonNull(dto.getPublicStatus()) ? dto.getPublicStatus() : PublicStatus.PUBLIC;
        this.isPublished = dto.isPublished();
        this.pollItems = Objects.nonNull(pollItems) ? pollItems : List.of();
        if (!isPublished) {
            this.pollStatus = PollStatus.TEMPORARY;
        } else {
            this.pollStatus = PollStatus.ONGOING;
        }

        for (PollItem item : pollItems) {
            item.belong(this);
        }
    }

    public static Post newPost(JwtUser jwtUser, CreatePostReq dto, List<PollItem> pollItems) {
        return new Post(jwtUser, dto, pollItems);
    }

    public static Post newPost(JwtUser user, FromArchive dto, List<PollItem> pollItems) {
        return new Post(user, dto, pollItems);
    }

    public PostResponse newPostResponse() {
        List<PollItemResponse> pollItemResponseList = new ArrayList<>();
        for (PollItem pollItem : pollItems) {
            PollItemResponse response = pollItem.newPollItemResponse();
            pollItemResponseList.add(response);
        }

        return new PostResponse(id, userId, nickname, profile, title, content, publicStatus, isPublished,
                pollStatus, pollItemResponseList, getUpdatedAt());
    }

    public Long getId() {
        return id;
    }

    public boolean checkValidity(UUID userId) {
        if (this.userId.equals(userId)) {
            return true;
        }
        return false;
    }

    public void update(CreatePostReq dto, List<PollItem> pollItems) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.publicStatus = dto.getPublicStatus();
        this.pollItems = pollItems;
        this.isPublished = dto.isPublished();
    }

    public List<Long> getItemList() {
        List<Long> itemList = new ArrayList<>();
        for (PollItem pollItem : pollItems) {
            itemList.add(pollItem.getId());
        }
        return itemList;
    }

    public PollItem getPollItem(Long itemId) {
        PollItem pollItem = null;
        for (PollItem item : pollItems) {
            if (item.getId().equals(itemId)) {
                pollItem = item;
            }
        }

        return pollItem;
    }

    public void endPoll() {
        this.pollStatus = PollStatus.CLOSED;
    }
}
