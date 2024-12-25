package ru.glebdos.usermicroservice.config;


import org.modelmapper.ModelMapper;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

@Configuration
public class UserConfig {


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


}
