package com.harmony.aop;

import com.harmony.board.Board;
import com.harmony.boardUser.BoardUser;
import com.harmony.boardUser.BoardUserEnum;
import com.harmony.boardUser.BoardUserRepository;
import com.harmony.comment.Comment;
import com.harmony.common.ApiResponseDto;
import com.harmony.security.UserDetailsImpl;
import com.harmony.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.concurrent.RejectedExecutionException;

@Slf4j(topic = "UserCheckAop")
@Aspect
@Component
@AllArgsConstructor

public class UserCheckAop {
    private final BoardUserRepository boardUserRepository;

    @Pointcut("execution(* com.harmony.board.BoardService.updateBoard(..))")
    private void updateBoard() {
    }

    @Pointcut("execution(* com.harmony.board.BoardService.deleteBoard(..))")
    private void deleteBoard() {
    }

    @Pointcut("execution(* com.harmony.comment.CommentService.updateComment(..))")
    private void updateComment() {}

//    @Pointcut("execution(* com.harmony.comment.CommentService.deleteComment(..))")
//    private void deleteComment() {}

    @Around("updateBoard() || deleteBoard()")
    public Object executePostRoleCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        // Board 받아옴
        Board board = (Board) joinPoint.getArgs()[0];

        // 로그인 회원이 없는 경우, 수행시간 기록하지 않음
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // 로그인 회원 정보
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User loginUser = userDetails.getUser();

        // Board를 create한 user 정보를 받아오기 위해 필요
        BoardUser boardUser = boardUserRepository.findByBoardAndRole(board, BoardUserEnum.ADMIN);

        if (auth.getPrincipal().getClass() == UserDetailsImpl.class) {
            // board를 create한 사람과 login한 user가 같은지 비교
            if (!(boardUser.getUser().equals(loginUser))) {
                log.warn("작성자만 Board 를 수정/삭제 할 수 있습니다.");
                throw new RejectedExecutionException();
            }
        }

        // 핵심기능 수행
        return joinPoint.proceed();
    }

    @Around("updateComment()")
    public Object executeCommentRoleCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        // Comment를 찾음
        Comment comment = (Comment) joinPoint.getArgs()[1];

        // 로그인한 기록이 없다면 수행시간을 기록하지 않음
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 로그인 한 회원의 정보
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User loginUser = userDetails.getUser();

        if (auth.getPrincipal().getClass() == UserDetailsImpl.class) {
            if (!(comment.getUser().equals(loginUser))) {
                log.warn("작성자만 수정/삭제할 수 있습니다");
                return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 수정/삭제할 수 있습니다", HttpStatus.BAD_REQUEST.value()));
            }
        }

        return joinPoint.proceed();
    }
}
