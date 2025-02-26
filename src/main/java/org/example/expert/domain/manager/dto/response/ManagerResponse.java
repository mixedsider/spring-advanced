package org.example.expert.domain.manager.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.dto.response.UserResponse;

@Getter
@RequiredArgsConstructor
public class ManagerResponse {

    private final Long id;
    private final UserResponse user;
}
