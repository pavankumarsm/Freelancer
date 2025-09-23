package org.example.freelancer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FreelancerApplication {

    public static void main(String[] args) {
        System.out.println("Test code to check write access in repo");
        SpringApplication.run(FreelancerApplication.class, args);
    }

}
