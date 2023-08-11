package com.harmony.user;


import com.harmony.aop.UserCheck;
import com.harmony.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    private final JwtUtil jwtUtil;


    public void signUp(SignupRequestDto signupRequestDto) throws SQLIntegrityConstraintViolationException {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        String nickname = signupRequestDto.getNickname();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("중복된 username 입니다");
        }

        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new SQLIntegrityConstraintViolationException("중복된 nickname 입니다");
        }

        User user = new User(username, password, nickname);
        userRepository.save(user);
    }

    public void login(LoginRequestDto loginRequestDto, HttpServletResponse res) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        User user = findUser(username);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("로그인 실패");
        }

        String token = jwtUtil.createToken(username);
        jwtUtil.addJwtToCookie(token, res);
    }

    @UserCheck
    @Transactional
    public UserResponseDto updateUser(User user, UserUpdateRequestDto updateRequestDto,
                                      User loginUser) {
        user.updateUser(updateRequestDto,
                updateRequestDto.getNickname(),
                updateRequestDto.getIntroduction());

        if (updateRequestDto.getPassword()!=null) {
            user.updatePassword(passwordEncoder.encode(updateRequestDto.getPassword()));
        }

        User savedUser = userRepository.save(user);
        return new UserResponseDto(savedUser);
    }

    @UserCheck
    @Transactional
    public void deleteUser(User user, User loginUser) {
        
        userRepository.delete(user);
    }


    public User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
    }

}
