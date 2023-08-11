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
    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */

    @Column(name = "comment_username")
    private String commentUsername;

    @Column(name = "comment_nickname")
    private String commentNickname;
    
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
