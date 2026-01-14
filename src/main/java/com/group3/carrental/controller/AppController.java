package com.group3.carrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.group3.carrental.entity.Utilisateur;

@Component
public class AppController {
    private UserRole currentUserRole = UserRole.Visitor;
    private Utilisateur currentUser = null;

    private final VisitorController visitorController;
    private final LoueurController loueurController;
    private final AgentController agentController;

    @Autowired
    public AppController(VisitorController visitorController, LoueurController loueurController,
            AgentController agentController) {
        this.visitorController = visitorController;
        this.loueurController = loueurController;
        this.agentController = agentController;
    }

    public enum UserRole {
        Visitor,
        Loueur,
        Agent,
        Exit
    }

    public void startApp() {
        while (currentUserRole != UserRole.Exit) {
            switch (currentUserRole) {
                case Visitor:
                    handleVisitorMenu();
                    break;
                case Loueur:
                    handleLoueurMenu();
                    break;
                case Agent:
                    handleAgentMenu();
                    break;
                default:
                    System.out.println("Choix invalide !");
                    break;
            }
        }
        System.out.println("Au revoir !");
    }

    private void handleVisitorMenu() {
        VisitorController.VisitorResult result = visitorController.displayMenuVisitor();
        if (result.exit) {
            currentUserRole = UserRole.Exit;
        } else if (result.user != null) {
            currentUser = result.user;
            switch (currentUser.getRole()) {
                case Loueur:
                    currentUserRole = UserRole.Loueur;
                    break;
                case Agent:
                    currentUserRole = UserRole.Agent;
                    break;
            }
        }
    }

    private void handleLoueurMenu() {
        Utilisateur result = loueurController.displayMenuLoueur(currentUser);
        if (result == null) {
            // Déconnexion
            currentUserRole = UserRole.Visitor;
            currentUser = null;
        }
    }

    private void handleAgentMenu() {
        Utilisateur result = agentController.displayMenuAgent(currentUser);
        if (result == null) {
            // Déconnexion
            currentUserRole = UserRole.Visitor;
            currentUser = null;
        }
    }
}
