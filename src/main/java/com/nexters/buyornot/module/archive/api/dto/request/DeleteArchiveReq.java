package com.nexters.buyornot.module.archive.api.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DeleteArchiveReq {
    List<Long> ids = new ArrayList<>();

    protected DeleteArchiveReq() {}

    public DeleteArchiveReq(List<Long> list) {
        this.ids = list;
    }
}
