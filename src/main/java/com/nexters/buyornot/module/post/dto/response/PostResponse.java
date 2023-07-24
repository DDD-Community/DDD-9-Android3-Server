package com.nexters.buyornot.module.post.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
@Getter
public class PostResponse {
    private Long id;
    private String userNickname;
    private String title;
    private String content;
    private String publicStatus;
    private String pollStatus;
    private List<PollItemResponse> pollItemResponseList;

    public PostResponse(Long id, String userNickname, String title, String content, String publicStatus, String pollStatus, List<PollItemResponse> pollItems) {
        this.id = id;
        this.userNickname = userNickname;
        this.title = title;
        this.content = content;
        this.publicStatus = publicStatus;
        this.pollStatus = pollStatus;
        this.pollItemResponseList = pollItems;
    }
}
