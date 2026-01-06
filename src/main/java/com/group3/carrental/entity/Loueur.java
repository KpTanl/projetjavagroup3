package com.group3.carrental.entity;

public class Loueur extends Utilisateur {

    public Loueur(int id, String nom, String prenom, String email, String motDePasse) {
        super(id, nom, prenom, email, motDePasse, Role.Loueur);
        // TODO Auto-generated constructor stub
    }

}
