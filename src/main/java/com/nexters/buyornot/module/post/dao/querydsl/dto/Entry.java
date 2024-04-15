package com.nexters.buyornot.module.post.dao.querydsl.dto;

import com.nexters.buyornot.module.post.domain.poll.Participant;
import com.nexters.buyornot.module.post.domain.poll.Unrecommended;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Entry {
    List<Participant> participants = new ArrayList<>();
    List<Unrecommended> unrecommended = new ArrayList<>();
}
