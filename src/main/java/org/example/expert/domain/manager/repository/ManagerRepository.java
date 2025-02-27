package org.example.expert.domain.manager.repository;

import org.example.expert.domain.manager.entity.Manager;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    @Query("SELECT m FROM Manager m WHERE m.todo.id = :todoId")
    @EntityGraph(attributePaths = {"user"})
    List<Manager> findByTodoIdWithUser(@Param("todoId") Long todoId);
}
