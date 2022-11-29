package com.weathermonitor.gaodeapi;

import com.weathermonitor.gaodeapi.config.HttpClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author 28252
 */
@SpringBootApplication
@EnableConfigurationProperties({HttpClientConfig.class})
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class GaoDeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GaoDeApiApplication.class, args);
    }

}
