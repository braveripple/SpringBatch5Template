package com.example.demo.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JobConfig {
	
	private final JobRepository jobRepository;
	private final PlatformTransactionManager txManager;

	@Bean
    Job doSomethingJob() {
		return new JobBuilder("doSomethingJob", jobRepository)
				.start(doSomethingStep())
				.build();
	}
	
	@Bean
	Step doSomethingStep() {
		return new StepBuilder("doSomethingStep", jobRepository).tasklet(new Tasklet() {

			private final Logger logger = LoggerFactory.getLogger(JobConfig.class);	
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				
				logger.info("Do Something!!");
				
				return RepeatStatus.FINISHED;
			}
		}, txManager).build();
	}

}