package com.nexters.buyornot.module.post.api.dto.request;

import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class CreatePostReq {

    @NotBlank
    private String title;
    private String content;
    private PublicStatus publicStatus;
    @NotNull
    private List<@NotBlank String> itemUrls;
    private boolean isPublished;

    public static CreatePostReq of(String title, String content, PublicStatus publicStatus, boolean isPublished, List<String> itemUrls) {
        return builder()
                .title(title)
                .content(content)
                .publicStatus(publicStatus)
                .isPublished(isPublished)
                .itemUrls(Objects.nonNull(itemUrls) ? itemUrls : List.of())
                .build();
    }
}
