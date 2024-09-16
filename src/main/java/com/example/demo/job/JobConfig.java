package com.example.demo.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.example.demo.job.tasklet.tablecopy.TableCopyPrimaryToSecondaryTasklet;
import com.example.demo.job.tasklet.tablecopy.TableCopySecondaryToPrimaryTasklet;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

	// #############################
	// # doSomethingJob
	// #############################

	@Bean
    Job doSomethingJob(JobRepository jobRepository, Step doSomethingStep) {
		return new JobBuilder("doSomethingJob", jobRepository)
				.start(doSomethingStep)
				.build();
	}
	
	
	@Bean
	Step doSomethingStep(JobRepository jobRepository, PlatformTransactionManager txManager) {
		return new StepBuilder("doSomethingStep", jobRepository).tasklet(new Tasklet() {

			private final Logger logger = LoggerFactory.getLogger(JobConfig.class);	
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				
				logger.info("Do Something!!");
	
				String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
			    
			    if (currentTransactionName != null) {
			        System.out.println("Current Transaction Name: " + currentTransactionName);
			    } else {
			        System.out.println("No active transaction.");
			    }
				return RepeatStatus.FINISHED;
			}
		}, txManager).build();
	}
	
//	// #############################
//	// # timecardJob
//	// #############################
//
//    @Bean
//    Job timecardJob(Step registTimecardStep, Step displayTimecardStep) throws Exception {
//		return jobBuilderFactory.get("timecardJob")
//				.incrementer(new RunIdIncrementer())
//				.start(registTimecardStep)
//				.next(displayTimecardStep)
//				.validator(timecardJobValidator())
//				.build();
//	}
//    
//	@Bean
//	JobParametersValidator timecardJobValidator() {
//		final DefaultJobParametersValidator validator = 
//				new DefaultJobParametersValidator();
//		validator.setRequiredKeys(new String[] {"name"});
//		return validator;
//	}
//
//	@Bean
//	Step registTimecardStep(RegistTimecardTasklet registTimecardTasklet) throws Exception {
//		return stepBuilderFactory.get("registTimecardStep")
//				.tasklet(registTimecardTasklet)
//				.build();
//	}
//	
//	@Bean
//	Step displayTimecardStep(DisplayTimecardTasklet displayTimecardTasklet) throws Exception {
//		return stepBuilderFactory.get("displayTimecardStep")
//				.tasklet(displayTimecardTasklet)
//				.build();
//	}
//

	// #############################
	// # tableCopyJob
	// #############################
	
    @Bean
    Job tableCopyJob(JobRepository jobRepository, Step tableCopySecondaryToPrimaryStep, Step tableCopyPrimaryToSecondaryStep) throws Exception {
		return new JobBuilder("tableCopyJob", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(tableCopySecondaryToPrimaryStep)
				.next(tableCopyPrimaryToSecondaryStep)
				.build();
	}
	
	@Bean
	Step tableCopySecondaryToPrimaryStep(JobRepository jobRepository, 
			PlatformTransactionManager txManager,
			TableCopySecondaryToPrimaryTasklet tasklet) throws Exception {
		return new StepBuilder("tableCopySecondaryToPrimaryStep", jobRepository)
				.tasklet(tasklet, txManager)
				.build();
	}
	
	@Bean
	Step tableCopyPrimaryToSecondaryStep(JobRepository jobRepository, 
			@Qualifier("secondaryTransactionManager") PlatformTransactionManager txManager,
			TableCopyPrimaryToSecondaryTasklet tasklet) throws Exception {
		return new StepBuilder("tableCopyPrimaryToSecondaryStep", jobRepository)
				.tasklet(tasklet, txManager)
				.build();
	}
//
//
//	// #############################
//	// # gateJob
//	// #############################
//	
//    @Bean
//    Job gateJob(Step gate1Step, Step gate2Step, Step gate3Step) throws Exception {
//		return jobBuilderFactory.get("gateJob")
//				.start(gate1Step)
//				.next(gate2Step)
//				.next(gate3Step)
//				.build();
//	}
//	
//	@Bean
//	Step gate1Step(Tasklet gateTasklet) throws Exception {
//		return stepBuilderFactory.get("gate1Step")
//				.tasklet(gateTasklet)
//				.build();
//	}
//	
//	@Bean
//	Step gate2Step(Tasklet gateTasklet) throws Exception {
//		return stepBuilderFactory.get("gate2Step")
//				.tasklet(gateTasklet)
//				.build();
//	}
//
//	@Bean
//	Step gate3Step(Tasklet gateTasklet) throws Exception {
//		return stepBuilderFactory.get("gate3Step")
//				.tasklet(gateTasklet)
//				.build();
//	}

}