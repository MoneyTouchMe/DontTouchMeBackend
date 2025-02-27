package com.example.donttouchme.member.domain;

import com.example.donttouchme.common.Entity.BaseEntity;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.member.domain.value.LoginProvider;
import com.example.donttouchme.member.domain.value.ROLE;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ROLE role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginProvider loginProvider;

    @Column
    private String contact;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private final List<Event> events = new ArrayList<>();

    @Builder(builderMethodName = "builderWithoutPassword", buildMethodName = "builderWithoutPassword")
    public Member(String name, String email, ROLE role, LoginProvider loginProvider) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.loginProvider = loginProvider;
    }

    @Builder(builderMethodName = "builderWithPassword", buildMethodName = "builderWithPassword")
    public Member(String name, String email, String password, ROLE role, LoginProvider loginProvider, String contact) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.loginProvider = loginProvider;
        this.contact = contact;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
