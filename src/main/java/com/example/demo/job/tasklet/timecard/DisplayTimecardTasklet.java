package com.example.demo.job.tasklet.timecard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Component
@RequiredArgsConstructor
public class DisplayTimecardTasklet implements Tasklet{
	 
	private final Logger logger = LoggerFactory.getLogger(DisplayTimecardTasklet.class);	

	private final JdbcTemplate primaryJdbcTemplate;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		logger.info("*** DisplayTimecardTasklet ***");

		try (Stream<Timecard> stream = primaryJdbcTemplate.queryForStream(
				"SELECT created_at, name FROM timecard WHERE created_at >= ? ORDER BY created_at DESC LIMIT 6;"
			  , new DataClassRowMapper<>(Timecard.class)
			  , LocalDate.now())
			) {
	    	logger.info("-------------------------------------------------------");
			stream.forEach(data -> {
		    	logger.info("日時：{}　名前：{}", 
		    			data.getCreatedAt().format(DateTimeFormatter.ofPattern( "yyyy/MM/dd HH:mm:ss.SSS")), data.getName());
		    	logger.info("-------------------------------------------------------");
		    });
		} catch (DataAccessException e) {
		    e.printStackTrace();
		}
		
		return RepeatStatus.FINISHED;
	}
	
	@Data
	@ToString
	private static class Timecard {
		private final LocalDateTime createdAt;
		private final String name;
	}

}
