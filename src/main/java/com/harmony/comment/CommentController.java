package com.harmony.comment;


import com.harmony.common.ApiResponseDto;
import com.harmony.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


// lombok
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/cards")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/{cardId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long cardId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto commentResponseDto = commentService.createComment(cardId, commentRequestDto, userDetails.getUser());
        return ResponseEntity.ok().body(commentResponseDto);
    }

    // 댓글 수정
    @PutMapping("/{cardId}/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long cardId, @PathVariable Long commentId,
                                                            @RequestBody CommentRequestDto commentRequestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("댓글 수정 시도");
        Comment comment = commentService.findComment(commentId);
        CommentResponseDto commentResponseDto = commentService.updateComment(cardId, comment, commentRequestDto);
        return ResponseEntity.ok().body(commentResponseDto);
    }

    // 댓글 삭제
    @DeleteMapping("/{cardId}/comments/{commentId}")
    public ResponseEntity<ApiResponseDto> deleteComment(@PathVariable Long cardId, @PathVariable Long commentId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("댓글 삭제 시도");
        Comment comment = commentService.findComment(commentId);
        ApiResponseDto apiResponseDto = commentService.deleteComment(cardId, comment);
        return ResponseEntity.ok().body(apiResponseDto);
    }

}
