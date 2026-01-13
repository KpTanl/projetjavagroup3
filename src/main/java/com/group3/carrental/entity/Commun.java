package com.group3.carrental.entity;

public interface Commun {
    Vehicule rechercherVehicule();

    Vehicule FiltreVehicule(); // Note: UML casing is technically FiltreVehicule, though standard Java is
                               // filtreVehicule. I'll stick to UML name or typical Java convention? User said
                               // "strictly follow UML". UML: + FiltreVehicule().

    void ConsulterProfilsAgents();
}
