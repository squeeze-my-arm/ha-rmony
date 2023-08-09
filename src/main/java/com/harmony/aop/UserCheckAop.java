package com.harmony.aop;

import com.harmony.board.Board;
import com.harmony.boardUser.BoardUser;
import com.harmony.boardUser.BoardUserEnum;
import com.harmony.boardUser.BoardUserRepository;
import com.harmony.security.UserDetailsImpl;
import com.harmony.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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

    @Around("updateBoard() || deleteBoard()")
    public Object executeBoardRoleCheck(ProceedingJoinPoint joinPoint) throws Throwable {
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
            if (!(boardUser.getUser().getUsername().equals(loginUser.getUsername()))) {
                log.warn("작성자만 Board 를 수정/삭제 할 수 있습니다.");
                throw new RejectedExecutionException();
            }
        }

        // 핵심기능 수행
        return joinPoint.proceed();
    }

    //로그인한 유저가 보드유저로 등록됐는지 확인
    @Around("@annotation(com.harmony.aop.BoardUserCheck)")
    public Object executeBoardUserCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("AOP executeBoardUserCheck 실행");

        // 로그인 회원이 없는 경우, 수행시간 기록하지 않음
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // 로그인 회원 정보
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User loginUser = userDetails.getUser();
        //boardUser 로 등록되어있지 않으면 error
        if (!boardUserRepository.existsByUserAndBoard_Id(loginUser, (Long) joinPoint.getArgs()[0])) {
            throw new RejectedExecutionException("보드에 등록된 유저와 일치하지 않습니다.");
        }
        log.info("AOP 통과");
        // 핵심기능 수행
        return joinPoint.proceed();
    }

    //로그인한 유저 본인 확인
    @Around("@annotation(com.harmony.aop.UserCheck)")
    public Object executeUserCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("AOP executeUserCheck 실행");

        // 로그인 회원이 없는 경우, 수행시간 기록하지 않음
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // 로그인 회원 정보
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User loginUser = userDetails.getUser();
        //로그인한 유저가 수정할 유저의 아이디와 일치하나?
        Long pathUserId = (Long) joinPoint.getArgs()[0];
        if (!loginUser.getId().equals(pathUserId)) {
            throw new RejectedExecutionException("본인의 유저 정보가 아닙니다. 수정 할 수 없습니다.");
        }
        log.info("AOP executeUserCheck 통과");
        // 핵심기능 수행
        return joinPoint.proceed();
    }

}
