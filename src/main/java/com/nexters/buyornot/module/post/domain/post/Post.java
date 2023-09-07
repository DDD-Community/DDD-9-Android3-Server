package com.nexters.buyornot.module.post.domain.post;

import com.nexters.buyornot.module.model.BaseEntity;
import com.nexters.buyornot.module.post.api.dto.request.CreatePostReq;
import com.nexters.buyornot.module.post.api.dto.request.FromArchive;
import com.nexters.buyornot.module.post.domain.model.PollStatus;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.nexters.buyornot.module.post.api.dto.response.PollItemResponse;
import com.nexters.buyornot.module.post.api.dto.response.PostResponse;
import com.nexters.buyornot.module.user.api.dto.JwtUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;


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
    private PublicStatus publicStatus = PublicStatus.PUBLIC;

    @ColumnDefault("true")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean isPublished;

    @Enumerated(EnumType.STRING)
    private PollStatus pollStatus = PollStatus.ONGOING;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PollItem> pollItems = new ArrayList<>();

    private Post(JwtUser user, CreatePostReq dto, List<PollItem> pollItems) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.profile = user.getProfile();
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.publicStatus = dto.getPublicStatus();
        this.isPublished = dto.isPublished();
        this.pollItems = pollItems;
        if(!isPublished) pollStatus = PollStatus.TEMPORARY;

        for(PollItem pollItem : pollItems) {
            pollItem.belong(this);
        }
    }

    private Post(JwtUser user, FromArchive dto, List<PollItem> pollItems) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.profile = user.getProfile();
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.publicStatus = dto.getPublicStatus();
        this.isPublished = dto.isPublished();
        this.pollItems = pollItems;
        if(!isPublished) pollStatus = PollStatus.TEMPORARY;

        for(PollItem item : pollItems) {
            item.belong(this);
        }
    }

    public static Post newPost(JwtUser jwtUser, CreatePostReq dto, List<PollItem> pollItems) {
        return new Post(jwtUser, dto, pollItems);
    }

    public static Post newPostFromArchive(JwtUser user, FromArchive dto, List<PollItem> pollItems) {
        return new Post(user, dto, pollItems);
    }

    public PostResponse newPostResponse() {
        List<PollItemResponse> pollItemResponseList = new ArrayList<>();
        for(PollItem pollItem : pollItems) {
            PollItemResponse response = pollItem.newPollItemResponse();
            pollItemResponseList.add(response);
        }

        LocalDateTime now = LocalDateTime.now();
        Period period = Period.between(this.getUpdatedAt().toLocalDate(), now.toLocalDate());
        Duration duration = Duration.between(this.getUpdatedAt().toLocalTime(), now.toLocalTime());
        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();
        long hours = duration.toHours();
        long minutes = duration.toMinutes() - hours * 60;
        long seconds = duration.getSeconds() - hours * 60 * 60 - minutes * 60;

        return new PostResponse(id, userId.toString(), nickname, profile, title, content, publicStatus, isPublished, pollStatus, pollItemResponseList, getUpdatedAt(), now, years, months, days, hours, minutes, seconds);
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
        this.isPublished = dto.isPublished();
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

    public void endPoll() {
        this.pollStatus = PollStatus.CLOSED;
    }

    public void publish(CreatePostReq dto, List<PollItem> pollItems) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.publicStatus = dto.getPublicStatus();
        this.pollItems = pollItems;
        this.isPublished = dto.isPublished();
    }
}
