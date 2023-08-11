package com.harmony.user;

import com.harmony.boardUser.BoardUser;
import com.harmony.cardUser.CardUser;
import com.harmony.comment.Comment;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

// lombok
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// jpa
@Entity
@Table(name = "users")
public class User {

    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 25)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false, unique = true, length = 25)
    private String nickname;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "google_id")
    private String googleId;

    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<BoardUser> boardUsers = new LinkedHashSet<>();


    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<CardUser> cardUsers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<Comment> comments = new LinkedHashSet<>();

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    @Builder
    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public User(String username, String password, String nickname, String googleId) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.googleId = googleId;
    }

    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */
    public void updateUser(String username, String password, String nickname, String introduction) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.introduction = introduction;
    }

    public User googleIdupdate(String googleUserInfoId) {
        this.googleId = googleUserInfoId;
        return this;
    }
}
