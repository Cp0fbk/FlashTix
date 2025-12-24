package com.flashtix;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlashtixApplication {
    private static Dotenv dotenv;

    public static void main(String[] args) {
        dotenv = Dotenv.configure()
                .directory(System.getProperty("user.dir"))
                .ignoreIfMissing()
                .systemProperties()
                .load();
        SpringApplication.run(FlashtixApplication.class, args);
    }

    public static Dotenv getEnv() {
        // Implementation for loading .env file if needed
        return dotenv;
    }
}
