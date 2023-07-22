package com.nexters.buyornot.module.post.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostResponse {
    private Long id;
    private String userNickname;
    private String title;
    private String content;
    private String publicStatus;
    private String pollStatus;
    private List<PollItemResponse> pollItemResponseList;

    public PostResponse(Long id, String title, String content, String publicStatus, String pollStatus, List<PollItemResponse> pollItems) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.publicStatus = publicStatus;
        this.pollStatus = pollStatus;
        this.pollItemResponseList = pollItems;
    }

    public void addWriter(String nickname) {
        this.userNickname = nickname;
    }
}
