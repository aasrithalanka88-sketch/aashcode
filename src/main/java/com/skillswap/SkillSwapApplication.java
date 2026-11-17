package com.skillswap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SkillSwapApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkillSwapApplication.class, args);
        System.out.println("\n" +
                "==========================================================\n" +
                "  🚀 Skill Swap Application is up and running!\n" +
                "  🌐 Web UI:       http://localhost:8080\n" +
                "  📊 H2 Database:  http://localhost:8080/h2-console\n" +
                "     JDBC URL:     jdbc:h2:mem:skillswapdb\n" +
                "     Username:     sa\n" +
                "     Password:     (leave empty)\n" +
                "==========================================================\n");
    }
}
