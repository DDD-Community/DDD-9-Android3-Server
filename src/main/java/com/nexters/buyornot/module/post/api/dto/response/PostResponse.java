package com.nexters.buyornot.module.post.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexters.buyornot.module.post.domain.model.PollStatus;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.nexters.buyornot.module.post.domain.post.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.checkerframework.checker.formatter.qual.Format;

import java.time.LocalDateTime;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String userId;
    private String userNickname;
    private String title;
    private String content;
    private PublicStatus publicStatus;
    private boolean isPublished;
    private PollStatus pollStatus;
    private List<PollItemResponse> pollItemResponseList;
    private boolean participateStatus = false;
    private PollResponse pollResponse;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    public PostResponse(Long id, String userId, String userNickname, String title, String content, PublicStatus publicStatus, boolean isPublished, PollStatus pollStatus, List<PollItemResponse> pollItems, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.userNickname = userNickname;
        this.title = title;
        this.content = content;
        this.publicStatus = publicStatus;
        this.isPublished = isPublished;
        this.pollStatus = pollStatus;
        this.pollItemResponseList = pollItems;
        this.updatedAt = updatedAt;
    }

    public PostResponse(Post post) {
        this.id = post.getId();
    }

    public void addPollResponse(PollResponse pollResponse) {
        this.pollResponse = pollResponse;
        participateStatus = true;
    }
}
