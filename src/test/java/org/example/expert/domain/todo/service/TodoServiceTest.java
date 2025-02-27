package org.example.expert.domain.todo.service;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Mock
    private WeatherClient weatherClient;

    @Test
    void 할일을_저장한다() {
        // given
        Long userId = 1L;
        String email = "test@test.com";
        UserRole userRole = UserRole.USER;
        AuthUser authUser = new AuthUser(userId, email, userRole);

        String title = "title";
        String contents = "contents";
        TodoSaveRequest todoSaveRequest = new TodoSaveRequest(title, contents);


        given(todoRepository.save(any(Todo.class)))
                .willAnswer(invocation -> {
                    Todo saved = invocation.getArgument(0);
                    ReflectionTestUtils.setField(saved, "id", 1L); // id 값 설정
                    return saved;
                });


        // when
        TodoSaveResponse todoSaveResponse = todoService.saveTodo(authUser, todoSaveRequest);

        // then
        assertEquals(todoSaveResponse.getTitle(), todoSaveRequest.getTitle());
        assertEquals(todoSaveResponse.getContents(), todoSaveRequest.getContents());
        assertEquals(todoSaveResponse.getUser().getId(), authUser.getId());
        assertEquals(todoSaveResponse.getUser().getEmail(), authUser.getEmail());
    }

    @Test
    void 할일_단일_조회() {
        // given
        Long userId = 1L;
        String email = "test@test.com";
        UserRole userRole = UserRole.USER;
        AuthUser authUser = new AuthUser(userId, email, userRole);

        String title = "title";
        String contents = "contents";
        TodoSaveRequest todoSaveRequest = new TodoSaveRequest(title, contents);

        User user = User.fromAuthUser(authUser);

        Todo newTodo = new Todo(
                title,
                contents,
                null,
                user
        );
        ReflectionTestUtils.setField(newTodo, "id", 1L);

        given(todoRepository.findByIdWithUser(newTodo.getId())).willReturn(Optional.of(newTodo));


        // when
        TodoResponse todoResponse = todoService.getTodo(1L);

        // then
        assertEquals(user.getId(), todoResponse.getUser().getId());
        assertEquals(todoResponse.getContents(), todoSaveRequest.getContents());
    }

    @Test
    void 할일_목록_다건_조회() {
        // given
        int page = 1;
        int size = 5;
        Pageable pageable = PageRequest.of(page - 1, size);

        User user = new User("test@test.com", "password", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L); // ID 설정

        Todo todo1 = new Todo("title1", "contents1", "Sunny", user);
        ReflectionTestUtils.setField(todo1, "id", 1L);
        ReflectionTestUtils.setField(todo1, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(todo1, "modifiedAt", LocalDateTime.now());

        Todo todo2 = new Todo("title2", "contents2", "Cloudy", user);
        ReflectionTestUtils.setField(todo2, "id", 2L);
        ReflectionTestUtils.setField(todo2, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(todo2, "modifiedAt", LocalDateTime.now());

        List<Todo> todoList = List.of(todo1, todo2);
        Page<Todo> todoPage = new PageImpl<>(todoList, pageable, todoList.size());

        given(todoRepository.findAllByOrderByModifiedAtDesc(pageable)).willReturn(todoPage);

        // when
        Page<TodoResponse> result = todoService.getTodos(page, size);

        // then
        assertEquals(2, result.getContent().size());
        assertEquals("title1", result.getContent().get(0).getTitle());
        assertEquals("contents1", result.getContent().get(0).getContents());
        assertEquals("Sunny", result.getContent().get(0).getWeather());
        assertEquals(1L, result.getContent().get(0).getUser().getId());
        assertEquals("test@test.com", result.getContent().get(0).getUser().getEmail());
    }
}