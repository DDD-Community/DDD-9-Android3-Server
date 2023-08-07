package com.nexters.buyornot.module.post.api.dto.request;

import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class FromArchive {
    private String title;
    private String content;
    private PublicStatus publicStatus;
}
