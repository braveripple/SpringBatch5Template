package com.example.demo.job;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest
public class HelloWorldJobTest {

	@Autowired
	private JobLauncherTestUtils myJobLauncherTestUtils;

	@Test
	public void testJob() throws Exception {
		final JobExecution jobExecution = myJobLauncherTestUtils.launchJob();
		assertThat(jobExecution.getExitStatus(), is(ExitStatus.COMPLETED));
	}
	
	@TestConfiguration
	static class JobTestConfig {
		@Bean
		JobLauncherTestUtils myJobLauncherTestUtils() {
			return new JobLauncherTestUtils() {
				@Override
				@Autowired
				public void setJob(@Qualifier("helloWorldJob") Job job) {
					super.setJob(job);
				}
			};
		}
	}

}
