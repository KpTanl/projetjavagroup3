package com.group3.carrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.group3.carrental.controller.AppController;

@SpringBootApplication
public class CarRentalApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CarRentalApplication.class, args);

        System.out.println("\n--- Bienvenue sur notre application de location de véhicules ---");
        System.out.println("Groupe 3\n");
        

        AppController appController = context.getBean(AppController.class);
        appController.startApp();
    }
}
