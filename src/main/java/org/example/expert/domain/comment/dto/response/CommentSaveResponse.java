package org.example.expert.domain.comment.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.dto.response.UserResponse;

@Getter
@Builder
@RequiredArgsConstructor
public class CommentSaveResponse {

    private final Long id;
    private final String contents;
    private final UserResponse user;
}
