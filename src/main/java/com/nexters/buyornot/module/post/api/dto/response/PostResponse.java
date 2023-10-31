package com.nexters.buyornot.module.post.api.dto.response;

import com.nexters.buyornot.module.post.domain.model.PollStatus;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.nexters.buyornot.module.post.domain.post.Post;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String userId;
    private String userNickname;
    private String userProfile;
    private String title;
    private String content;
    private PublicStatus publicStatus;
    private boolean isPublished;
    private PollStatus pollStatus;
    private List<PollItemResponse> pollItemResponseList;
    private boolean participateStatus = false;
    private PollResponse pollResponse;
    private LocalDateTime updatedAt;
    private LocalDateTime now;

    int years, months, days;
    long hours, minutes, seconds;

    public PostResponse(Long id, String userId, String userNickname, String profile, String title, String content, PublicStatus publicStatus, boolean isPublished, PollStatus pollStatus, List<PollItemResponse> pollItems, LocalDateTime updatedAt, LocalDateTime now, int years, int months, int days, long hours, long minutes, long seconds) {
        this.id = id;
        this.userId = userId;
        this.userNickname = userNickname;
        this.userProfile = profile;
        this.title = title;
        this.content = content;
        this.publicStatus = publicStatus;
        this.isPublished = isPublished;
        this.pollStatus = pollStatus;
        this.pollItemResponseList = pollItems;
        this.updatedAt = updatedAt;
        this.now = now;
        this.years = years;
        this.months = months;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public void addPollResponse(PollResponse pollResponse) {
        this.pollResponse = pollResponse;
        participateStatus = true;
    }

    public void addArchiveStatusByItem(List<PollItemResponse> pollItemResponses) {
        this.pollItemResponseList = pollItemResponses;
    }
}
