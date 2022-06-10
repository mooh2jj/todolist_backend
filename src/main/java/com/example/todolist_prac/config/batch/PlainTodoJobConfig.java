package com.example.todolist_prac.config.batch;

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
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PlainTodoJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final TodoRepository todoRepository;

    private static final int CHUNK_SIZE = 4;

    @Bean("todoJob")
    public Job todoJob(Step todoStep) {
        return jobBuilderFactory.get("todoJob")
                .incrementer(new RunIdIncrementer())
                .start(todoStep)
                .build();
    }

    @JobScope
    @Bean("todoStep")
    public Step todoStep(ItemReader<TodoEntity> todoReader,
//                         ItemProcessor todoProcessor,
                         ItemWriter<TodoEntity> todoWriter) {
        return stepBuilderFactory.get("todoStep")
                .<TodoEntity, TodoEntity>chunk(CHUNK_SIZE)
                .reader(todoReader)
//                .processor(todoProcessor)
                .writer(todoWriter)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<TodoEntity> todoReader() {
        return new RepositoryItemReaderBuilder<TodoEntity>()
                .name("todoReader")
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
    public ItemWriter<TodoEntity> todoWriter() {
        return list -> log.info("write items.\n" +
                list.stream()
                        .map(s -> s.toString())
                        .collect(Collectors.joining("\n")));
    }

}
