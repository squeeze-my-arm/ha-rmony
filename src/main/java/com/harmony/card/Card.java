package com.harmony.card;

import com.harmony.cardUser.CardUser;
import com.harmony.column.Columns;
import com.harmony.common.Timestamped;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
// jpa
@Entity
@Table(name = "card")
public class Card extends Timestamped {

  /**
   * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "card_id", nullable = false, updatable = false)
  private Long id;

  @Column(name = "card_name", nullable = false, unique = true)
  private String cardname;

  @Column(name = "card_desc")
  private String description;

  @Column(name = "card_color", nullable = false)
  private String color;

  @Column(name = "deadline")
  private LocalDate deadline;

  @Column(name = "card_order")
  private Long cardOrder;

  /**
   * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
   */
  @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<CardUser> cardUsers = new LinkedHashSet<>();

  @ManyToOne
  @JoinColumn(name = "column_id")
  private Columns column;

  /**
   * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
   */


  /**
   * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
   */
  public void updateCard(CardRequestDto requestDto) {
    if (requestDto.getCardName() != null) {
      this.cardname = requestDto.getCardName();
    }
    if (requestDto.getColor() != null) {
      this.color = requestDto.getColor();
    }
    if (requestDto.getDeadline() != null) {
      this.deadline = requestDto.getDeadline();
    }
    if (requestDto.getDesc() != null) {
      this.description = requestDto.getDesc();
    }
    if (requestDto.getColumn() != null) {
      this.column = requestDto.getColumn();
    }
    if (requestDto.getCardOrder() != null) {
      this.cardOrder = requestDto.getCardOrder();
    }
  }

  public void addCardUser(CardUser cardUser) {
    this.cardUsers.add(cardUser);
  }

  public void removeCardUser(CardUser cardUser) {
    this.cardUsers.remove(cardUser);
  }

  public void clearCardUsers() {
    this.cardUsers.clear();
  }
}
