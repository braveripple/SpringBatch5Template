package com.example.demo.job;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.demo.component.PGCopy;

@Configuration
public class DataSourceConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.primary")
	@Primary
	DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.jobrepository")
	@BatchDataSource
	DataSource jobrepositoryDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.secondary")
	DataSource secondaryDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "primaryTransactionManager")
	@Primary
	PlatformTransactionManager primaryTransactionManager() {
		return new DataSourceTransactionManager(primaryDataSource());
	}
	
	@Bean(name = "secondaryTransactionManager")
	PlatformTransactionManager secondaryTransactionManager() {
		return new DataSourceTransactionManager(secondaryDataSource());
	}
	
	// JdbcTemplateのBean定義
	
	@Bean
	JdbcTemplate primaryJdbcTemplate() {
		return new JdbcTemplate(primaryDataSource());
	}
	
	@Bean
	JdbcTemplate secondaryJdbcTemplate() {
		return new JdbcTemplate(secondaryDataSource());
	}
	
	// PGCopyのBean定義
	
	@Bean
	PGCopy primaryPGCopy() {
		return new PGCopy(primaryDataSource());
	}
	
	@Bean
	PGCopy secondaryPGCopy() {
		return new PGCopy(secondaryDataSource());
	}
}
