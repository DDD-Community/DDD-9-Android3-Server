package com.nexters.buyornot.module.auth.event;

import com.nexters.buyornot.module.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeletedUserEvent {

    private User user;

    public DeletedUserEvent(User user) {
        this.user = user;
    }

    public static DeletedUserEvent of(User user) {
        return new DeletedUserEvent(user);
    }
}
