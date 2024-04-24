package com.alioth.server.domain.notification.domain;

import com.alioth.server.common.domain.BaseEntity;
import com.alioth.server.domain.member.domain.SalesMembers;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notification_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SM_id")
    private SalesMembers salesMember;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ReadStatus readStatus;

    @Column(unique = true) // 고유 식별자 필드 추가
    private String messageId;

}
