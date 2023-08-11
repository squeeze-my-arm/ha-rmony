package com.harmony.comment;

import com.harmony.card.Card;
import com.harmony.card.CardService;
import com.harmony.common.ApiResponseDto;
import com.harmony.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CardService cardService;
    private final CommentRepository commentRepository;

    // 댓글 작성
    public CommentResponseDto createComment(Long cardid, CommentRequestDto commentRequestDto, User user) {
        // 해당 카드 존재 확인
        Card card = cardService.findCard(cardid);

        // 댓글 생성
        Comment comment = new Comment(commentRequestDto, card, user);

        // 카드에 댓글 추가
        card.addComment(comment);

        // Repository에 댓글 저장
        Comment savedComment = commentRepository.save(comment);

        // CommentResponseDto에 담아서 return
        return new CommentResponseDto(savedComment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long cardid, Comment comment, CommentRequestDto commentRequestDto) {
        // 댓글 내용 수정
        comment.update(commentRequestDto);

        // 댓글 저장
        Comment updatedComment = commentRepository.save(comment);

        // CommentResponseDto에 담아서 return
        return new CommentResponseDto(updatedComment);
    }

    // 댓글 삭제
    public ApiResponseDto deleteComment(Long cardid, Comment comment) {
        // 카드 존재 확인
        Card card = cardService.findCard(cardid);

        // 카드에 있는 댓글도 삭제
        card.getComments().remove(comment);
        
        // 댓글 삭제
        commentRepository.delete(comment);

        // 상태코드와 메시지 반환
        return new ApiResponseDto("댓글 삭제 완료", HttpStatus.OK.value());
    }

    // 댓글 찾기
    public Comment findComment(Long commentid) {
        return commentRepository.findById(commentid).orElseThrow(IllegalArgumentException::new);
    }

}
