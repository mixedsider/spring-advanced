package org.example.expert.domain.user.service;

import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    void user를_조회를_성공한다(){
        // given
        Long userId = 1L;
        String userEmail = "test@test.com";
        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(user, "email", userEmail);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        UserResponse userResponse = userService.getUser(userId);

        // then
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isEqualTo(userId);
        assertThat(userResponse.getEmail()).isEqualTo(userEmail);
    }

    @Test
    void 없는_유저를_조회한다(){
        // given
        Long userId = 1L;

        // when && then
        assertThrows(InvalidRequestException.class, () -> userService.getUser(userId), "User not found");
    }

    @Test
    void 비밀번호_변경_시_유저가_없는_경우() {
        // given
        Long userId = 1L;
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest();

        // when && then
        assertThrows(InvalidRequestException.class, () -> userService.changePassword(userId, userChangePasswordRequest), "User not found");
    }

    @Test
    void 비밀번호_변경_시_기존_비밀번호와_같은_경우() {
        Long userId = 1L;
        String userEmail = "test@test.com";
        String encodedPassword = "vrvxfbvnrtcfibASDSDAF12345";
        String rawPassword = "asdfASDF123";
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest();
        ReflectionTestUtils.setField(userChangePasswordRequest, "oldPassword", rawPassword);
        ReflectionTestUtils.setField(userChangePasswordRequest, "newPassword", "fevrvdASDF123");


        given(passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), encodedPassword)).willReturn(true);

        User user = new User(userEmail, encodedPassword, UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when && then
        assertThrows(InvalidRequestException.class, () -> userService.changePassword(userId, userChangePasswordRequest), "새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
    }

    @Test
    void 비밀번호_변경_시_기존비밀번호와_입력비밀번호가_다른경우() {
        // given
        Long userId = 1L;
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest();
        ReflectionTestUtils.setField(userChangePasswordRequest, "oldPassword", "asdfASDF123");
        ReflectionTestUtils.setField(userChangePasswordRequest, "newPassword", "fevrvdASDF123");

        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(user, "password", "asdfASDF123");

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        assertThrows(InvalidRequestException.class, () -> userService.changePassword(userId, userChangePasswordRequest), "잘못된 비밀번호입니다.");
    }

    @Test
    void 비밀번호_변경에_성공한다() {
        // given
        Long userId = 1L;
        String userEmail = "test@test.com";
        String encodedPassword = "vrvxfbvnrtcfibASDSDAF12345";
        String rawPassword = "asdfASDF123";
        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest();
        ReflectionTestUtils.setField(userChangePasswordRequest, "oldPassword", rawPassword);
        ReflectionTestUtils.setField(userChangePasswordRequest, "newPassword", "fevrvdASDF123");


        given(passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), encodedPassword)).willReturn(false);
        given(passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), encodedPassword)).willReturn(true);

        User user = new User(userEmail, encodedPassword, UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        userService.changePassword(userId, userChangePasswordRequest);

        // then
        User findUser = userRepository.findById(userId).get();

        assertEquals(findUser, user);
    }

}