package com.podolist.podolist.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql="UPDATE task SET deleted=true,deletedAt=CURRENT_TIMESTAMP where id=?")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name="memberId")
    @Column(nullable = false)
    private Member member;

    @OneToOne
    @JoinColumn(name="categoryId")
    @Column(nullable = false)
    private Category category;
    private String details;

    @CreatedDate
    @Column(updatable=false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable =false)
    private LocalDateTime updatedAt;

    private boolean deleted;
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private LocalDateTime completedAt;

}
