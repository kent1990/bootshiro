package com.usthe.bootshiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.mybatis.spring.annotation.MapperScan;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 *
 * @author tomsun28
 * @date
 */
@SpringBootApplication
@MapperScan("com.usthe.bootshiro.dao")
@ServletComponentScan
public class BootshiroApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootshiroApplication.class, args);
	}

}
