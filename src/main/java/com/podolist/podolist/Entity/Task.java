package com.podolist.podolist.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@SQLDelete(sql="UPDATE task SET deleted=true,deletedAt=CURRENT_TIMESTAMP where id=?")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name="categoryId")
    private Category category;
    private String memo;

    @CreatedDate
    @Column(updatable=false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable =false)
    private LocalDateTime updatedAt;

    @ColumnDefault("false")
    private Boolean deleted;
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    // @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime startedAt;

    
    private LocalDateTime completedAt;
}
