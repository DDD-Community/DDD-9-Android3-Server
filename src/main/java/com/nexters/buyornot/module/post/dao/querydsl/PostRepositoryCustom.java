package com.nexters.buyornot.module.post.dao.querydsl;

import com.nexters.buyornot.module.post.api.dto.response.PostResponse;
import com.nexters.buyornot.module.post.dao.querydsl.dto.Entry;
import com.nexters.buyornot.module.post.dao.querydsl.dto.MyPostCondition;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepositoryCustom {
    PostResponse getPost(Long postId);

    Slice<PostResponse> getMain(Pageable pageable);

    Slice<PostResponse> getMine(MyPostCondition condition);

    List<PostResponse> findTemporaries(UUID userId);

    Entry getParticipants(Long postId);
}
