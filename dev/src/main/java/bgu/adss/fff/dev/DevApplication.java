package bgu.adss.fff.dev;

import bgu.adss.fff.dev.frontend.FrontendApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevApplication.class, args);
        new Thread(() -> {
            FrontendApp app = new FrontendApp();
        }).start();
    }

}
