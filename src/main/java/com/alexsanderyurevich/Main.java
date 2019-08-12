package com.alexsanderyurevich;

import com.alexsanderyurevich.loader.DeclineXmlLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    static {
        DeclineXmlLoader.loadDeclineData();
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
