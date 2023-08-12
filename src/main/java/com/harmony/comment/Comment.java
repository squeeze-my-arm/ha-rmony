package com.harmony.comment;


import com.harmony.card.Card;
import com.harmony.common.Timestamped;
import com.harmony.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// lombok
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

// jpa
@Entity
@Table(name = "comments")
public class Comment extends Timestamped {

    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment_content")
    private String commentContent;

    @Column(name = "comment_username")
    private String commentUsername;

    @Column(name = "comment_nickname")
    private String commentNickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public Comment(CommentRequestDto commentRequestDto, Card card, User user) {
        this.commentContent = commentRequestDto.getCommentContent();
        this.card = card;
        this.user = user;
        this.commentUsername = user.getUsername();
        this.commentNickname = user.getNickname();
    }

    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */
    public void update(CommentRequestDto commentRequestDto) {
        this.commentContent = commentRequestDto.getCommentContent();
    }

    public void setCard(Card card) {
        this.card = card;
    }

}
