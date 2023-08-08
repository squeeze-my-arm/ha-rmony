package com.harmony.aop;

import com.harmony.boardUser.BoardUserRepository;
import com.harmony.security.UserDetailsImpl;
import com.harmony.user.User;
import java.util.concurrent.RejectedExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j(topic = "BoardUserCheckAOP")
@Aspect
@Component
@RequiredArgsConstructor
public class BoardUserCheckAop {
  private final BoardUserRepository boardUserRepository;

  //로그인한 유저가 card 관련 기능을 사용할 때 BoardUser 에 등록되어있는지 확인->List<BoardUser>.
  @Around("@annotation(com.harmony.aop.BoardUserCheck)")
  public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
    log.info("AOP 실행");

    // 로그인 회원이 없는 경우, 수행시간 기록하지 않음
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    // 로그인 회원 정보
    UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
    User loginUser = userDetails.getUser();
    //boardUser 로 등록되어있지 않으면 error
    if(!boardUserRepository.existsByUserAndBoard_Id(loginUser, (Long) joinPoint.getArgs()[0])){
      throw new RejectedExecutionException("보드에 등록된 유저와 일치하지 않습니다.");
    }
    log.info("AOP 통과");
    // 핵심기능 수행
    return joinPoint.proceed();
  }
}
