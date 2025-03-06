package com.example.donttouchme.event.domain;

import com.example.donttouchme.common.Entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
public class SendValue extends BaseEntity { //연락처
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String value; //휴대폰 번호 or 이메일 주소

    @Builder
    public SendValue(String value) {
        this.value = value;
    }
}
