package com.lt.fly;

import com.lt.fly.jpa.BaseRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@ServletComponentScan
@EntityScan("com.lt.fly")
@EnableJpaRepositories(basePackages = "com.lt.fly",
        repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = { "com.lt.fly" })
public class FlyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlyApplication.class, args);
    }

}
