package org.example.expert.domain.auth.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthExceptionTest {

    @Test
    void AuthException_발생() {
        // given
        String errorMessage = "테스트 코드입니다.";

        // when
        AuthException exception = assertThrows(AuthException.class, () -> {
            throw new AuthException(errorMessage);
        });

        // then
        assertEquals(errorMessage, exception.getMessage());
    }

}