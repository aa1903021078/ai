package om.imooc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.imooc"})
public class StoreApp {

    public static void main(String[] args) {
        SpringApplication.run(StoreApp.class, args);
    }
}
