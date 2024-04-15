package com.nexters.buyornot.module.post.dao.querydsl.dto;

import com.nexters.buyornot.module.post.domain.model.PollStatus;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
public class MyPostCondition {
    private UUID userId;
    PollStatus pollStatus;
    boolean isPublished;
    Pageable pageable;

    public MyPostCondition(UUID userId, PollStatus pollStatus, boolean isPublished, PageRequest pageable) {
        this.userId = userId;
        this.pollStatus = pollStatus;
        this.isPublished = isPublished;
        this.pageable = pageable;
    }
}
