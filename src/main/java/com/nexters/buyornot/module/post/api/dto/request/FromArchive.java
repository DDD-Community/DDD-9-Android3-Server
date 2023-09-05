package com.nexters.buyornot.module.post.api.dto.request;

import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class FromArchive {
    @NotBlank
    private String title;
    private String content;
    private PublicStatus publicStatus;
    private boolean isPublished;
}
