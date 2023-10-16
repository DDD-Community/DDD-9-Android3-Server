package com.nexters.buyornot.module.archive.application;

import com.nexters.buyornot.global.common.codes.SuccessCode;
import com.nexters.buyornot.global.exception.BusinessExceptionHandler;
import com.nexters.buyornot.module.archive.api.dto.request.DeleteArchiveReq;
import com.nexters.buyornot.module.archive.api.dto.response.ArchiveResponse;
import com.nexters.buyornot.module.archive.dao.ArchiveRepository;
import com.nexters.buyornot.module.archive.domain.Archive;
import com.nexters.buyornot.module.item.dao.ItemRepository;
import com.nexters.buyornot.module.item.domain.Item;
import com.nexters.buyornot.module.item.event.SavedItemEvent;
import com.nexters.buyornot.module.model.Role;
import com.nexters.buyornot.module.post.dao.PostRepository;
import com.nexters.buyornot.module.user.api.dto.JwtUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.nexters.buyornot.global.common.codes.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArchiveService {

    private final ArchiveRepository archiveRepository;
    private final ItemRepository itemRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PostRepository postRepository;

    @Transactional
    public ArchiveResponse saveFromWeb(JwtUser user, String itemUrl) {
        if(user.getRole().equals(Role.NON_MEMBER.getValue())) throw new BusinessExceptionHandler(UNAUTHORIZED_USER_EXCEPTION);

        eventPublisher.publishEvent(
                SavedItemEvent.of(itemUrl)
        );

        Item item = itemRepository.findByItemUrl(itemUrl).get();

        Optional<Archive> oldArchive = archiveRepository.findByUserAndUrl(user.getId().toString(), itemUrl);

        if (oldArchive.isPresent()) {
            log.info("이미 저장된 상품입니다. 상품 정보를 업데이트합니다.");
            oldArchive.get().update(item.getUpdatedInfo());
            log.info("업데이트 완료");
            return oldArchive.get().newResponse();
        }

        Archive archive = item.newArchive(user.getId().toString());
        Archive savedArchive = archiveRepository.save(archive);
        ArchiveResponse response = savedArchive.newResponse();
        return response;
    }

//    @Transactional
    public ArchiveResponse saveFromPost(JwtUser user, Long itemId) {
        if(user.getRole().equals(Role.NON_MEMBER.getValue())) throw new BusinessExceptionHandler(UNAUTHORIZED_USER_EXCEPTION);

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_ITEM_EXCEPTION));

        Optional<Archive> oldArchive = archiveRepository.findByUserAndItem(user.getId().toString(), itemId);

        // TODO: 변경 데이터 감지
        if (oldArchive.isPresent()) {
            log.info("이미 저장된 상품입니다. 상품 정보를 업데이트합니다.");
            oldArchive.get().update(item.getUpdatedInfo());
            log.info("업데이트 완료");
            return oldArchive.get().newResponse();
        }

        Archive archive = item.newArchive(user.getId().toString());
        Archive savedArchive = archiveRepository.save(archive);
        ArchiveResponse response = savedArchive.newResponse(); // TODO: save transaction 타이밍
        return response;
    }

    @Transactional
    public ArchiveResponse likeArchive(JwtUser user, Long archiveId) {
        if (user.getRole().equals(Role.NON_MEMBER.getValue()))
            throw new BusinessExceptionHandler(UNAUTHORIZED_USER_EXCEPTION);
        Archive archive = archiveRepository.findById(archiveId)
                .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_ARCHIVE_EXCEPTION));

        archive.like();
        archiveRepository.save(archive);

        return archive.newResponse();
    }

    public List<ArchiveResponse> getAll(JwtUser user, final int page, final int count) {
        if (user.getRole().equals(Role.NON_MEMBER.getValue()))
            throw new BusinessExceptionHandler(UNAUTHORIZED_USER_EXCEPTION);

        String userId = user.getId().toString();

        List<ArchiveResponse> list = archiveRepository.findPageByUserIdOrderByUpdatedAtDesc(userId, PageRequest.of(page, count))
                .stream()
                .map(Archive::newResponse)
                .collect(Collectors.toList());

        return list;
    }

    public List<ArchiveResponse> getLikes(JwtUser user, final int page, final int count) {
        if (user.getRole().equals(Role.NON_MEMBER.getValue()))
            throw new BusinessExceptionHandler(UNAUTHORIZED_USER_EXCEPTION);

        String userId = user.getId().toString();

        List<ArchiveResponse> list = archiveRepository.findPageByUserIdAndIsLikedOrderByUpdatedAtDesc(userId, true, PageRequest.of(page, count))
                .stream()
                .map(Archive::newResponse)
                .collect(Collectors.toList());

        return list;
    }

    @Transactional
    public String delete(JwtUser user, DeleteArchiveReq deleteArchiveReq) {
        if (user.getRole().equals(Role.NON_MEMBER.getValue()))
            throw new BusinessExceptionHandler(UNAUTHORIZED_USER_EXCEPTION);

        for (Long id : deleteArchiveReq.getIds()) {
            Archive archive = archiveRepository.findByIdAndUserId(id, user.getId().toString())
                    .orElseThrow(() -> new BusinessExceptionHandler(NOT_FOUND_ARCHIVE_EXCEPTION));

            if (archive.verifyValidity(user.getId().toString())) {
                archive.delete();
            }
            archiveRepository.save(archive);
        }
        return SuccessCode.DELETE_SUCCESS.getMessage();
    }

    @Transactional
    public void deleteAll(String userId) {
        archiveRepository.deleteAllByUserId(userId);
    }
}
