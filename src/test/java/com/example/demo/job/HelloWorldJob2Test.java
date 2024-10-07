package com.example.demo.job;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest
public class HelloWorldJob2Test {

	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private Job helloWorldJob;

	@Test
	public void testMyJob() throws Exception {
		// JobParametersを作成
		JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
		// ジョブを実行
		JobExecution jobExecution = jobLauncher.run(helloWorldJob, jobParameters);
		// 実行結果の検証
		assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
	}
}
