package com.group3.carrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.group3.carrental.controller.AppController;

@SpringBootApplication
public class CarRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentalApplication.class, args);
        System.out.println("Bienvenue sur notre application de louer un vehicule ");
        System.out.println("nos nom est Group 3");
        AppController.startApp();
    }

}
