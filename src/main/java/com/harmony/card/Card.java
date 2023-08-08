package com.harmony.card;

import com.harmony.column.Columns;
import com.harmony.comment.Comment;
import com.harmony.common.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// jpa
@Entity
@Table(name="cards")
public class Card extends Timestamped {
  /**
   * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="card_id", nullable = false, updatable = false)
  private Long id;

  @Column(name="card_name", nullable = false, unique = true, length = 25)
  private String cardname;

  @Column(name="card_desc", nullable = false)
  private String description;

  @Column(name="card_color", nullable = false)
  private String color;

  @Column(name="deadline")
  private String deadline;

  @Column(name="card_order")
  private Long cardOrder;

  /**
   * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
   */


  /**
   * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
   */
  @ManyToOne
  @JoinColumn(name = "column_id")
  private Columns columns;

  @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE)   // 카드가 삭제되면 해당 카드에 존재하는 댓글도 함께 삭제
  @Column(name = "comments")
  private List<Comment> comments = new ArrayList<>();;

  /**
   * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
   */


  /**
   * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
   */
  public void addComment(Comment comment) {
    this.comments.add(comment);
    comment.setCard(this);
  }

}
