package com.nexters.buyornot.module.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass //BaseEntity를 상속한 엔티티들은 아래 필드들을 컬럼으로 인식
@EntityListeners({AuditingEntityListener.class})
public abstract class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, columnDefinition = "varchar(10) default 'ACTIVE'")
    private EntityStatus entityStatus = EntityStatus.ACTIVE;

    public void delete() {
        this.entityStatus = EntityStatus.DELETED;
    }

    public void restore() {
        this.entityStatus = EntityStatus.ACTIVE;
    }
}
