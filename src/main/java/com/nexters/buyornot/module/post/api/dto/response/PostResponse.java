package com.nexters.buyornot.module.post.api.dto.response;

import com.nexters.buyornot.module.post.domain.model.PollStatus;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.querydsl.core.annotations.QueryProjection;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@ToString
public class PostResponse {
    private Long id;
    private UUID userId;
    private String userNickname;
    private String userProfile;
    private String title;
    private String content;
    private PublicStatus publicStatus;
    private boolean isPublished;
    private PollStatus pollStatus;
    private List<PollItemResponse> pollItemResponses;
    private boolean participateStatus;
    private PollResponse pollResponse;
    private LocalDateTime updatedAt;
    private LocalDateTime now;
    int years, months, days;
    long hours, minutes, seconds;

    @QueryProjection
    public PostResponse(Long id, UUID userId, String userNickname, String profile, String title, String content,
                        PublicStatus publicStatus, boolean isPublished, PollStatus pollStatus,
                        List<PollItemResponse> pollItems, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.userNickname = userNickname;
        this.userProfile = profile;
        this.title = title;
        this.content = content;
        this.publicStatus = publicStatus;
        this.isPublished = isPublished;
        this.pollStatus = pollStatus;
        this.pollItemResponses = pollItems;
        this.updatedAt = updatedAt;
        addTime();
    }

    @QueryProjection
    public PostResponse(Long id, UUID userId, String userNickname, String profile, String title, String content,
                        PublicStatus publicStatus, boolean isPublished, PollStatus pollStatus,
                        LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.userNickname = userNickname;
        this.userProfile = profile;
        this.title = title;
        this.content = content;
        this.publicStatus = publicStatus;
        this.isPublished = isPublished;
        this.pollStatus = pollStatus;
        this.updatedAt = updatedAt;
        addTime();
    }

    public void addPollResponse(PollResponse pollResponse) {
        this.pollResponse = pollResponse;
        participateStatus = true;
    }


    public void addArchiveStatusByItem(List<PollItemResponse> pollItemResponses) {
        this.pollItemResponses = pollItemResponses;
    }

    private void addTime() {
        LocalDateTime now = LocalDateTime.now();
        Period period = Period.between(updatedAt.toLocalDate(), now.toLocalDate());
        Duration duration = Duration.between(updatedAt.toLocalTime(), now.toLocalTime());
        this.years = period.getYears();
        this.months = period.getMonths();
        this.days = period.getDays();
        this.hours = duration.toHours();
        this.minutes = duration.toMinutes() - hours * 60;
        this.seconds = duration.getSeconds() - hours * 60 * 60 - minutes * 60;
    }
}
