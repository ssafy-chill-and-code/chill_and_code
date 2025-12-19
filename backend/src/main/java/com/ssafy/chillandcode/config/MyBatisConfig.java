package com.ssafy.chillandcode.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(
    basePackages = { "com.ssafy.chillandcode.model.dao", "com.ssafy.chillandcode.recommend.place" },
    annotationClass = Mapper.class
)
public class MyBatisConfig {

}
