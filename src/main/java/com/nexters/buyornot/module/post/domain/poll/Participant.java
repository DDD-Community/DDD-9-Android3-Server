package com.nexters.buyornot.module.post.domain.poll;

import com.nexters.buyornot.module.model.BaseEntity;
import com.nexters.buyornot.module.post.domain.post.PollItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "poll_participant")
public class Participant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long id;

    @Column(length = 255)
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_item_id")
    private PollItem pollItem;

    public Participant(PollItem item, String userId) {
        this.userId = userId;
        this.pollItem = item;
    }

    public static Participant newPoll(PollItem item, String userId) {
        return new Participant(item, userId);
    }

    public Long getPollItemId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }
}
