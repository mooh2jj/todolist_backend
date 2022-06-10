package com.example.todolist_prac.config.batch;

import com.example.todolist_prac.model.todo.TodoEntity;
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
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class AllReadJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private static final int CHUNK_SIZE = 4;
    private static final int FETCH_SIZE = 4;

    @Bean("allReadJob")
    public Job allReadJob(
            Step allReadStep
    ) {
        return jobBuilderFactory.get("allReadJob")
                .incrementer(new RunIdIncrementer())
                .start(allReadStep)
                .build();
    }
// allReadPagingReader
    @JobScope
    @Bean("allReadStep")
    public Step allReadStep(
            ItemReader<TodoEntity> allReadCursorReader,
            ItemWriter<TodoEntity> allReadWriter
    ) {
        return stepBuilderFactory.get("allReadStep")
                .<TodoEntity, TodoEntity>chunk(CHUNK_SIZE)
                .reader(allReadCursorReader)
                .writer(allReadWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @StepScope
    @Bean
    public JdbcCursorItemReader<TodoEntity> allReadCursorReader() {
        return new JdbcCursorItemReaderBuilder<TodoEntity>()
                .verifyCursorPosition(false)
                .fetchSize(FETCH_SIZE)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(TodoEntity.class))
                .sql("select * from todo order by id")
                .name("jdbcCursorItemReader")
                .build();
    }

    @StepScope
    @Bean
    public JdbcPagingItemReader<TodoEntity> allReadPagingReader(
            PagingQueryProvider queryProvider) {
        return new JdbcPagingItemReaderBuilder<TodoEntity>()
                .pageSize(CHUNK_SIZE)
                .fetchSize(FETCH_SIZE)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(TodoEntity.class))
                .queryProvider(queryProvider)
                .name("jdbcCursorItemReader")
                .build();
    }

    @Bean
    public PagingQueryProvider queryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("*");
        queryProvider.setFromClause("from todo");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("id", Order.ASCENDING);

        queryProvider.setSortKeys(sortKeys);
        return queryProvider.getObject();
    }

    @StepScope
    @Bean
    public ItemWriter<TodoEntity> allReadWriter() {
        return list -> log.info("write items.\n" +
                list.stream()
                        .map(s -> s.toString())
                        .collect(Collectors.joining("\n")));
    }
}
