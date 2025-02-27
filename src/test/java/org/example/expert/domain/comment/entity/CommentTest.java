package org.example.expert.domain.comment.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {


    @Test
    void comment를_수정한다() {

        Comment comment = new Comment();

        String contents = "contents";

        comment.update(contents);

        assertEquals(comment.getContents(), contents);
    }
}