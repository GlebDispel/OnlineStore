<<<<<<<< HEAD:ProductMicroservice/src/main/java/ru/glebdos/ws/productmicroservice/config/ProductConfig.java
package ru.glebdos.ws.productmicroservice.config;
========
package ru.glebdos.usermicroservice.config;
>>>>>>>> b1e0211d53ea7f470c3fc802e5d5ffdd5f21a10f:UserMicroservice/src/main/java/ru/glebdos/usermicroservice/config/UserConfig.java


import org.modelmapper.ModelMapper;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

@Configuration
<<<<<<<< HEAD:ProductMicroservice/src/main/java/ru/glebdos/ws/productmicroservice/config/ProductConfig.java
public class ProductConfig {
========
public class UserConfig {

>>>>>>>> b1e0211d53ea7f470c3fc802e5d5ffdd5f21a10f:UserMicroservice/src/main/java/ru/glebdos/usermicroservice/config/UserConfig.java

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


}
