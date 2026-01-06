package com.example.projetjavagroup3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Scanner;

@SpringBootApplication
public class Projetjavagroup3Application {

    public static void main(String[] args) {
        SpringApplication.run(Projetjavagroup3Application.class, args);
        System.out.println("Hello World! et bienvenue sur notre application et entre votre nom");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
        System.out.println("Hello " + name);
    }

}
