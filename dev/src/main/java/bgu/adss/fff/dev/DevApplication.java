package bgu.adss.fff.dev;

import bgu.adss.fff.dev.frontend.FrontendApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevApplication {

    public static boolean IS_DEBUG;

    public static void main(String[] args) {
        if(args.length > 0) {
            IS_DEBUG = args[0].equals("-d");
        }

        SpringApplication.run(DevApplication.class, args);
        new Thread(() -> {
            FrontendApp app = new FrontendApp();
        }).start();
    }

}
