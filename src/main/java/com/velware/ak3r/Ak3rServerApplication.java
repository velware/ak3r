package com.velware.ak3r;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Ak3rServerApplication implements CommandLineRunner {

    @Value("${server.hostname}")
    private String hostname;
    @Value("${server.port}")
    private Integer port;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Ak3rServerApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {

        NioServer server = new NioServer(hostname, port);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
