package com.nexters.buyornot.module.post.api.dto.request;

import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class CreatePostReq {

    @NotBlank
    private String title;
    private String content;
    private PublicStatus publicStatus;
    private List<String> itemUrls = new ArrayList<>();
    private boolean isPublished;

    public static CreatePostReq of(String title, String content, PublicStatus publicStatus, boolean isPublished, List<String> itemUrls) {
        return builder()
                .title(title)
                .content(content)
                .publicStatus(publicStatus)
                .isPublished(isPublished)
                .itemUrls(itemUrls)
                .build();
    }
}
