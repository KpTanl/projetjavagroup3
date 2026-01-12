package com.group3.carrental.entity;

public class Test_Parking {
    public static void main(String[] args) {
        // 1. On crée un véhicule avec TES paramètres : 
        // type, marque, modele, couleur, etat, rue, cp, ville
        Vehicule v1 = new Vehicule(
            Vehicule.TypeVehicule.Voiture, 
            "Tesla", 
            "Model 3", 
            "Rouge",                       // La couleur que j'avais oubliée
            Vehicule.EtatVehicule.Non_loué, 
            "Rue de la Paix", 
            "75000", 
            "Paris"
        );

        // 2. Initialisation option
        v1.setOptionRetour(Vehicule.OptionRetour.retour_classique);

        // 3. Création de l'agent propriétaire
        Agent agent = new AgentParticulier(); 
        agent.setId(1); 
        v1.setAgent(agent);

        // 4. Test d'activation (Cas passant)
        System.out.println("État initial : " + v1.getOptionRetour());
        agent.activerOptionParkingVehicule(v1);
        System.out.println("Après activation par l'agent : " + v1.getOptionRetour());

        // 5. TEST DE SÉCURITÉ (Il doit être DANS le main)
        Agent autreAgent = new AgentParticulier();
        autreAgent.setId(2); // ID différent
        
        try {
            System.out.println("Tentative d'activation par un autre agent...");
            autreAgent.activerOptionParkingVehicule(v1);
        } catch (IllegalStateException e) {
            // Si on arrive ici, c'est que notre sécurité fonctionne !
            System.out.println("Succès du test de sécurité : " + e.getMessage());
        }

    }
}