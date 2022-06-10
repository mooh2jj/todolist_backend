package com.example.todolist_prac.config.batch;

import com.example.todolist_prac.components.MailComponents;
import com.example.todolist_prac.model.todo.TodoEntity;
import com.example.todolist_prac.dto.todo.TodoNotificationDto;
import com.example.todolist_prac.repository.todo.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TodoNotificationJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final TodoRepository todoRepository;

    private static final int CHUNK_SIZE = 4;

    @Bean("todoNotificationJob")
    public Job todoNotificationJob(Step todoNotificationStep) {
        return jobBuilderFactory.get("todoNotificationJob")
                .incrementer(new RunIdIncrementer())
                .start(todoNotificationStep)
                .build();
    }

    @JobScope
    @Bean("todoNotificationStep")
    public Step todoNotificationStep(ItemReader<TodoEntity> todoNotificationReader,
                         ItemProcessor<TodoEntity, TodoNotificationDto> todoNotificationProcessor,
                         ItemWriter<TodoNotificationDto> todoNotificationWriter) {
        return stepBuilderFactory.get("todoNotificationStep")
                .<TodoEntity, TodoNotificationDto>chunk(CHUNK_SIZE)
                .reader(todoNotificationReader)
                .processor(todoNotificationProcessor)
                .writer(todoNotificationWriter)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<TodoEntity> todoNotificationReader() {
        return new RepositoryItemReaderBuilder<TodoEntity>()
                .name("todoNotificationReader")
                .repository(todoRepository)
                .methodName("findBy")
                .pageSize(CHUNK_SIZE)
                .arguments(List.of())
                .sorts(Collections.singletonMap("id", Sort.Direction.DESC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<TodoEntity, TodoNotificationDto> todoNotificationProcessor() {
        return todo -> {
            // todo들 아직 compleated 안된 거 count로 표기
            List<TodoEntity> todoList = todoRepository.findByCompletedFalse();

            if (todoList.isEmpty()) {
                return null;
            }

            return TodoNotificationDto.builder()
                    .id(todo.getId())
                    .title(todo.getTitle())
                    .order(todo.getOrder())
                    .completed(todo.getCompleted())
                    .count(todoList.size())
                    .build();
        };
    }


    @StepScope
    @Bean
    public ItemWriter<TodoNotificationDto> todoNotificationWriter(MailComponents mailService) {
        return items -> items.forEach(
                // mailSendService로 대체, todo갯수만큼 보내짐 mail -> dto.getMail로 각자 보내기!
                item -> mailService.sendMail("ehtjd33@gmail.com", item.getTitle(), item.toMessage())
        );
    }

//    @StepScope
//    @Bean
//    public ItemWriter<TodoNotificationDto> todoNotificationWriter() {
//        return items -> {
//            items.forEach(item -> System.out.println(item.toMessage()));
//            System.out.println("==== chunk is finished");
//        };
//    }

}
