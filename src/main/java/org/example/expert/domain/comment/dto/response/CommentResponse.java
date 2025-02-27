package org.example.expert.domain.comment.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.dto.response.UserResponse;

@Getter
@RequiredArgsConstructor
public class CommentResponse {

    private final Long id;
    private final String contents;
    private final UserResponse user;

}
