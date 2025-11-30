package com.ssafy.chillandcode.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan("com.ssafy.chillandcode.model.dao")
@Configuration
public class MyBatisConfig {

}
