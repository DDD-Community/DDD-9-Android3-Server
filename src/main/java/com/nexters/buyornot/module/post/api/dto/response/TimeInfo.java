package com.nexters.buyornot.module.post.api.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimeInfo {
    private LocalDateTime now;
    int years, months, days;
    long hours, minutes, seconds;
}
