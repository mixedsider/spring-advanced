package org.example.expert.domain.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.enums.UserRole;

@Getter
@RequiredArgsConstructor
public class AuthUser {

    private final Long id;
    private final String email;
    private final UserRole userRole;
}
