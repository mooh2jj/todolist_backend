package com.example.todolist_prac.config.batch;

import com.example.todolist_prac.adapter.FakeSendService;
import com.example.todolist_prac.model.TodoEntity;
import com.example.todolist_prac.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
    public Job todoJob(Step todoNotificationStep) {
        return jobBuilderFactory.get("todoNotificationJob")
                .incrementer(new RunIdIncrementer())
                .start(todoNotificationStep)
                .build();
    }

    @JobScope
    @Bean("todoNotificationStep")
    public Step todoNotificationStep(ItemReader<TodoEntity> todoNotificationReader,
//                         ItemProcessor todoProcessor,
                         ItemWriter<TodoEntity> todoNotificationWriter) {
        return stepBuilderFactory.get("todoNotificationStep")
                .<TodoEntity, TodoEntity>chunk(CHUNK_SIZE)
                .reader(todoNotificationReader)
//                .processor(todoProcessor)
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

//    @StepScope
//    @Bean
//    public ItemProcessor<TodoEntity, String> todoProcessor() {
//        return item -> "processed " + item.getText();
//    }


    @StepScope
    @Bean
    public ItemWriter<TodoEntity> todoNotificationWriter(FakeSendService fakeSendService) {
        return items -> items.forEach(item -> fakeSendService.send(item.getTitle(), String.valueOf(item.getCompleted())));
    }

}
