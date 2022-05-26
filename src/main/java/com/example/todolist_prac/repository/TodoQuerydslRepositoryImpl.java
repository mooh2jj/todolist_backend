package com.example.todolist_prac.repository;

import com.example.todolist_prac.model.QTodoEntity;
import com.example.todolist_prac.model.TodoEntity;
import com.example.todolist_prac.model.TodoSearchCondition;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class TodoQuerydslRepositoryImpl implements CustomTodoRepository {

    private final JPAQueryFactory queryFactory;

    public TodoQuerydslRepositoryImpl(EntityManager em) {
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
