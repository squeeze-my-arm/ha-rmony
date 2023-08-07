package com.harmony.boardUser;

import com.harmony.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {
    List<BoardUser> findAllByUser(User user);
}
