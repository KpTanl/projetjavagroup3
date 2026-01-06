package com.group3.carrental.entity;

public class Agent extends Utilisateur {

    public Agent(int id, String nom, String prenom, String email, String motDePasse) {
        super(id, nom, prenom, email, motDePasse, Role.Agent);
        // TODO Auto-generated constructor stub
    }

}
