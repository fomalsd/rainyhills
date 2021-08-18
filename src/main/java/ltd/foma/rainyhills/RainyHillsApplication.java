package ltd.foma.rainyhills;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RainyHillsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RainyHillsApplication.class, args);
    }

}
