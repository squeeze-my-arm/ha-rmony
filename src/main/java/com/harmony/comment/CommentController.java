package com.harmony.comment;


import com.harmony.common.ApiResponseDto;
import com.harmony.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


// lombok
@RequiredArgsConstructor

// spring web
@RestController
@RequestMapping("/api/cards")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/{cardid}/comments")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long cardid, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto commentResponseDto = commentService.createComment(cardid, commentRequestDto, userDetails.getUser());
        return ResponseEntity.ok().body(commentResponseDto);
    }

    // 댓글 수정
    @PutMapping("/{cardid}/comments/{commentid}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long cardid, @PathVariable Long commentid,
                                                            @RequestBody CommentRequestDto commentRequestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Comment comment = commentService.findComment(commentid);
        CommentResponseDto commentResponseDto = commentService.updateComment(cardid, comment, commentRequestDto);
        return ResponseEntity.ok().body(commentResponseDto);
    }

    // 댓글 삭제
    @DeleteMapping("/{cardid}/comments/{commentid}")
    public ResponseEntity<ApiResponseDto> deleteComment(@PathVariable Long cardid, @PathVariable Long commentid,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Comment comment = commentService.findComment(commentid);
        ApiResponseDto apiResponseDto = commentService.deleteComment(cardid, comment);
        return ResponseEntity.ok().body(apiResponseDto);
    }

}
