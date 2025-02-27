package org.example.expert.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.domain.user.service.UserAdminService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAdminController.class)
class UserAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAdminService userAdminService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void changeUserRole를_호출한다() throws Exception {
        // given
        long userId = 1L;
        long userId2 = 2L;
        String email = "test@test.com";

        User user = new User(email, "password", UserRole.ADMIN);
        ReflectionTestUtils.setField(user, "id", userId);

        User user2 = new User(email, "password", UserRole.USER);
        ReflectionTestUtils.setField(user2, "id", userId2);

        UserRoleChangeRequest userRoleChangeRequest = new UserRoleChangeRequest(UserRole.ADMIN.name());

        given(userRepository.findById(eq(userId2))).willReturn(Optional.of(user2));
        willDoNothing().given(userAdminService).changeUserRole(eq(userId2), any(UserRoleChangeRequest.class));

        // when & then
        mockMvc.perform(patch("/admin/users/{userId}", userId2) // <- 여기서 요청 URL 확인
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userRoleChangeRequest))
                        .requestAttr("userId", userId)
                        .requestAttr("email", email)
                        .requestAttr("userRole", UserRole.ADMIN.name()))
                .andExpect(status().isOk());


        verify(userAdminService, times(1)).changeUserRole(eq(userId2), any(UserRoleChangeRequest.class));
    }

    @Test
    void changeUserRole를_호출_시_Authexception발생 () throws Exception {
        // given
        long userId = 1L;
        long userId2 = 2L;
        String email = "test@test.com";

        User user = new User(email, "password", UserRole.ADMIN);
        ReflectionTestUtils.setField(user, "id", userId);

        User user2 = new User(email, "password", UserRole.USER);
        ReflectionTestUtils.setField(user2, "id", userId2);

        UserRoleChangeRequest userRoleChangeRequest = new UserRoleChangeRequest(UserRole.ADMIN.name());

        given(userRepository.findById(eq(userId2))).willReturn(Optional.of(user2));
        willDoNothing().given(userAdminService).changeUserRole(eq(userId2), any(UserRoleChangeRequest.class));

        // when & then
        mockMvc.perform(patch("/admin/users/{userId}", userId2) // <- 여기서 요청 URL 확인
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userRoleChangeRequest))
                        .requestAttr("userId", userId)
                        .requestAttr("email", email)
                        .requestAttr("userRole", UserRole.USER.name()))
                .andExpect(status().isUnauthorized());
    }

}