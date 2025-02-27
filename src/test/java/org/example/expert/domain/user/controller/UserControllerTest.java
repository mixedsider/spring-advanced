package org.example.expert.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void User_단일_조회() throws Exception {
        //given
        long userId = 1L;
        String email = "test@test.com";
        UserResponse userResponse = new UserResponse(userId, email);

        given(userService.getUser(userId)).willReturn(userResponse);

        // when && then
        MvcResult mvcResult = mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value(email))
                .andReturn();

        // then (ResponseEntity 검증)
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus()); // HTTP 상태 코드 검증
    }

    @Test
    // 리턴값이 void 인 곳은 gpt 를 사용하였습니다.
    // 새벽에 하느라 튜터님들이 안계셔서 사용한것입니다.
    void User_비밀번호_변경_전체_커버리지() throws Exception {
        // given
        long userId = 1L;
        String email = "test@test.com";
        // 비밀번호 변경 요청 객체 생성
        UserChangePasswordRequest passwordRequest = new UserChangePasswordRequest("oldPassword", "newPassword");

        // when
        doNothing().when(userService).changePassword(eq(userId), any(UserChangePasswordRequest.class));

        // when & then
        MvcResult mvcResult = mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(passwordRequest))
                        // AuthUserArgumentResolver가 기대하는 속성들을 설정합니다.
                        .requestAttr("userId", userId)
                        .requestAttr("email", email)
                        .requestAttr("userRole", UserRole.USER.name()))
                .andExpect(status().isOk())
                .andReturn();

        // then: userService.changePassword가 올바른 인자로 호출되었는지 검증
        verify(userService).changePassword(eq(userId), refEq(passwordRequest));

        // then (ResponseEntity 검증)
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus()); // HTTP 상태 코드 검증
    }
}