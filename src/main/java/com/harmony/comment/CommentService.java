package com.harmony.comment;

import com.harmony.card.Card;
import com.harmony.card.CardService;
import com.harmony.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CardService cardService;
    private final CommentRepository commentRepository;

    // 댓글 작성
    public CommentResponseDto createComment(Long cardid, CommentRequestDto commentRequestDto, User user) {
        Card card = cardService.findCard(cardid);

        Comment comment = new Comment(commentRequestDto, card, user);

        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment);
    }


}
