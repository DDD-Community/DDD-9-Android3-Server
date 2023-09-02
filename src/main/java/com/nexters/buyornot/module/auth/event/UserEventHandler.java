package com.nexters.buyornot.module.auth.event;

import com.nexters.buyornot.module.archive.application.ArchiveService;
import com.nexters.buyornot.module.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UserEventHandler {
    private final ArchiveService archiveService;

    private User user;

    @Async
    @TransactionalEventListener
    public void deleteUser(DeletedUserEvent event) throws IOException {
        this.user = event.getUser();

        //delete archive
        archiveService.deleteAll(user.getId().toString());
    }
}
