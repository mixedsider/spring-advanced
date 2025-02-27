package org.example.expert.domain.todo.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {


    @Test
    void todoEntity를_업데이트한다() {
        String title = "title";
        String contents = "contents";
        Todo todo = new Todo();

        todo.update(title, contents);

        assertEquals(title, todo.getTitle());
        assertEquals(contents, todo.getContents());
    }
}