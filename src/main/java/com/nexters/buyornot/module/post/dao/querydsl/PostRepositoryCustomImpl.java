package com.nexters.buyornot.module.post.dao.querydsl;

import static com.nexters.buyornot.module.archive.domain.QArchive.archive;
import static com.nexters.buyornot.module.post.domain.poll.QParticipant.participant;
import static com.nexters.buyornot.module.post.domain.poll.QUnrecommended.unrecommended;
import static com.nexters.buyornot.module.post.domain.post.QPollItem.pollItem;
import static com.nexters.buyornot.module.post.domain.post.QPost.post;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;

import com.nexters.buyornot.module.post.api.dto.response.PollItemResponse;
import com.nexters.buyornot.module.post.api.dto.response.PostResponse;
import com.nexters.buyornot.module.post.dao.querydsl.dto.Entry;
import com.nexters.buyornot.module.post.dao.querydsl.dto.MyPostCondition;
import com.nexters.buyornot.module.post.domain.model.PublicStatus;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory query;

    //todo: 단건 조회 아이템들 리스트 형식으로 들어가도록 수정

    @Override
    public PostResponse getPost(Long postId) {
//        List<PostResponse> response = query.select(
//                        Projections.constructor(PostResponse.class,
//                                post.id, post.userId, post.nickname, post.profile, post.title, post.content,
//                                post.publicStatus, post.isPublished, post.pollStatus,
//                                list(
//                                        Projections.constructor(PollItemResponse.class,
//                                                pollItem.id, pollItem.itemId, pollItem.itemUrl, pollItem.brand,
//                                                pollItem.itemName, pollItem.imgUrl, pollItem.itemPrice.value,
//                                                pollItem.itemPrice.discountRate,
//                                                pollItem.itemPrice.discountedPrice, archive.isNull())
//                                ), post.updatedAt)
//                ).from(post)
//                .join(post.pollItems, pollItem)
//                .leftJoin(archive).on(archive.itemId.eq(pollItem.itemId))
//                .where(post.id.eq(postId))
//                .fetch();
//

        PostResponse response = query.select(
                        Projections.constructor(PostResponse.class,
                                post.id, post.userId, post.nickname, post.profile, post.title, post.content,
                                post.publicStatus, post.isPublished, post.pollStatus,
                                GroupBy.list(
                                        Projections.constructor(PollItemResponse.class,
                                                pollItem.id, pollItem.itemId, pollItem.itemUrl, pollItem.brand,
                                                pollItem.itemName, pollItem.imgUrl, pollItem.itemPrice.value,
                                                pollItem.itemPrice.discountRate,
                                                pollItem.itemPrice.discountedPrice, archive.isNull())
                                ), post.updatedAt)
                ).from(post)
                .join(post.pollItems, pollItem)
                .leftJoin(archive).on(archive.itemId.eq(pollItem.itemId))
                .where(post.id.eq(postId))
                .groupBy(post.id)
                .fetchOne();

        log.info("[dao] " + response.toString());

        return response;
    }

    @Override
    public Slice<PostResponse> getMain(Pageable pageable) {
        List<PostResponse> posts = query.selectFrom(post)
                .join(post.pollItems, pollItem)
                .leftJoin(archive).on(archive.itemId.eq(pollItem.itemId))
                .where(post.isPublished
                        .and(post.publicStatus.eq(PublicStatus.PUBLIC)))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .transform(
                        groupBy(post.id).list(
                                Projections.constructor(PostResponse.class,
                                        post.id, post.userId, post.nickname, post.profile, post.title, post.content,
                                        post.publicStatus, post.isPublished, post.pollStatus,
                                        GroupBy.list(
                                                Projections.constructor(PollItemResponse.class,
                                                        pollItem.id, pollItem.itemId, pollItem.itemUrl, pollItem.brand,
                                                        pollItem.itemName, pollItem.imgUrl, pollItem.itemPrice.value,
                                                        pollItem.itemPrice.discountRate,
                                                        pollItem.itemPrice.discountedPrice, archive.isNull())
                                        ), post.updatedAt)
                        )
                );

        return new SliceImpl<>(posts, pageable, hasNextPage(posts, pageable.getPageSize()));
    }

    @Override
    public Slice<PostResponse> getMine(MyPostCondition condition) {
        List<PostResponse> posts = query.select(
                        Projections.constructor(PostResponse.class,
                                post.id, post.userId, post.nickname, post.profile, post.title, post.content,
                                post.publicStatus, post.isPublished, post.pollStatus,
                                list(
                                        Projections.constructor(PollItemResponse.class,
                                                pollItem.id, pollItem.itemId, pollItem.itemUrl, pollItem.brand,
                                                pollItem.itemName, pollItem.imgUrl, pollItem.itemPrice.value,
                                                pollItem.itemPrice.discountRate,
                                                pollItem.itemPrice.discountedPrice, archive.isNull())
                                ), post.updatedAt)
                ).from(post)
                .join(post.pollItems, pollItem)
                .leftJoin(participant).on(participant.pollItem.id.eq(pollItem.id))
                .leftJoin(unrecommended).on(unrecommended.post.id.eq(post.id))
                .leftJoin(archive).on(archive.itemId.eq(pollItem.itemId))
                .where(searchMine(condition))
                .offset(condition.getPageable().getOffset())
                .limit(condition.getPageable().getPageSize())
                .distinct()
                .fetch();

        return new SliceImpl<>(posts, condition.getPageable(),
                hasNextPage(posts, condition.getPageable().getPageSize()));
    }

    @Override
    public Entry getParticipants(Long postId) {
        Entry entry = query.select(Projections.constructor(Entry.class,
                        list(participant),
                        list(unrecommended)
                ))
                .from(post)
                .where(post.id.eq(postId))
                .join(post.pollItems, pollItem)
                .leftJoin(participant).on(participant.pollItem.id.eq(pollItem.id))
                .leftJoin(unrecommended).on(unrecommended.post.id.eq(post.id))
                .distinct()
                .fetchOne();
        return entry;
    }

    @Override
    public List<PostResponse> findTemporaries(UUID userId) {
        List<PostResponse> temporaries = query.select(
                        Projections.constructor(PostResponse.class,
                                post.id, post.userId, post.nickname, post.profile, post.title, post.content,
                                post.publicStatus, post.isPublished, post.pollStatus,
                                list(
                                        Projections.constructor(PollItemResponse.class,
                                                pollItem.id, pollItem.itemId, pollItem.itemUrl, pollItem.brand,
                                                pollItem.itemName, pollItem.imgUrl, pollItem.itemPrice.value,
                                                pollItem.itemPrice.discountRate,
                                                pollItem.itemPrice.discountedPrice)
                                ), post.updatedAt)
                ).from(post)
                .join(post.pollItems, pollItem)
                .where(post.userId.eq(userId)
                        .and(post.isPublished.eq(false)))
                .orderBy(post.id.desc())
                .fetch();

        return temporaries;
    }

    private BooleanExpression searchMine(MyPostCondition condition) {
        return userEq(condition)
                .and(pollStatusEq(condition))
                .and(publishedEq(condition));
    }

    private BooleanExpression userEq(MyPostCondition condition) {
        return post.userId.eq(condition.getUserId());
    }

    private BooleanExpression pollStatusEq(MyPostCondition condition) {
        return post.pollStatus.eq(condition.getPollStatus());
    }

    private BooleanExpression publishedEq(MyPostCondition condition) {
        return post.isPublished.eq(condition.isPublished());
    }

    private boolean hasNextPage(List<PostResponse> posts, int pageSize) {
        if (posts.size() > pageSize) {
            posts.remove(pageSize);
            return true;
        }
        return false;
    }

}
