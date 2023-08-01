package com.nexters.buyornot.module.post.api.dto.request;

import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class CreatePostReq {

    private String title;
    private String content;
    private PublicStatus publicStatus;
    private List<String> itemUrls;

    public static CreatePostReq of(String title, String content, PublicStatus publicStatus, List<String> itemUrls) {
        return builder()
                .title(title)
                .content(content)
                .publicStatus(publicStatus)
                .itemUrls(itemUrls)
                .build();
    }
}
