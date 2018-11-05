package com.ef.job;

import com.ef.data.model.AccessLog;
import com.ef.data.repository.AccessLogRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@Slf4j
@RequiredArgsConstructor
class AccessLogReportJobConfig {

    private static final String ACCESS_LOG_REPORT_JOB = "ACCESS_LOG_REPORT_JOB";
    private static final String CHUNK_STEP = "CHUNK_STEP";
    private static final String OVERRIDDEN_BY_EXPRESSION = null;
    private static final AccessLogRepository OVERRIDDEN_ACCESS_LOG_BY_EXPRESSION = null;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Value("${batchStep.chunkSize}")
    private int chunkSize;

    @Bean
    public Job accessLogReportJob() {
        return jobBuilderFactory.get(ACCESS_LOG_REPORT_JOB).incrementer(new RunIdIncrementer()).start(chunkStep())
                .build();
    }

    @Bean
    public Step chunkStep() {
        return stepBuilderFactory.get(CHUNK_STEP).<AccessLog, AccessLog>chunk(chunkSize)
                .reader(reader(OVERRIDDEN_BY_EXPRESSION)).processor(processor(OVERRIDDEN_ACCESS_LOG_BY_EXPRESSION))
                .writer(writer()).build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<AccessLog> reader(@Value("#{jobParameters[filePath]}") final String filePath) {
        return new AccessLogReader(filePath);
    }

    @Bean
    @StepScope
    public ItemProcessor<AccessLog, AccessLog> processor(AccessLogRepository accessLogRepository) {
        LocalDateTime latestAccessDate = Optional.ofNullable(accessLogRepository.findLatestAccessDate())
                .orElse(LocalDateTime.MIN);
        CompositeItemProcessor<AccessLog, AccessLog> compositeItemProcessor = new CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(Collections.singletonList(new DateFilterProcessor(latestAccessDate)));
        return compositeItemProcessor;
    }

    @Bean
    @StepScope
    public ItemWriter<AccessLog> writer() {
        return new AccessLogWriter();
    }
}
