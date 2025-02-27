package org.example.expert.domain.user.entity;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {


    @Test
    void 유저_생성자_테스트() {
        Long userId = 1L;
        String userEmail = "test@test.com";
        UserRole userRole = UserRole.USER;

        AuthUser authUser = new AuthUser(userId, userEmail, userRole);

        User user = User.fromAuthUser(authUser);

        assertEquals(authUser.getId(), user.getId());
        assertEquals(authUser.getEmail(), user.getEmail());
        assertEquals(authUser.getUserRole(), user.getUserRole());
    }
}