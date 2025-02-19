package com.example.donttouchme.common.user.domain;

import com.example.donttouchme.common.Entity.BaseEntity;
import com.example.donttouchme.common.user.domain.value.ROLE;
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
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private ROLE role;

    @Builder
    public User(String username, String name, String email, ROLE role) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
