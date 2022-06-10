package com.example.todolist_prac.repository;

import com.example.todolist_prac.model.QTodoEntity;
import com.example.todolist_prac.model.TodoEntity;
import com.example.todolist_prac.model.TodoSearchCondition;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class TodoQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public TodoQuerydslRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    QTodoEntity todo = QTodoEntity.todoEntity;


    public List<TodoEntity> findAll_querydsl() {
        return queryFactory
                .selectFrom(todo)
                .fetch();
    }

    public List<TodoEntity> search(TodoSearchCondition condition) {
        return queryFactory
                .select(todo)
                .from(todo)
                .where(todo.title.contains(condition.getTitle()),
                        todo.order.goe(condition.getTodoOrder()))
                .fetch();
    }

//    private BooleanExpression statusEq( statusCond) {

}
