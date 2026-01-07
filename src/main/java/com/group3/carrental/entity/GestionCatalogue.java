package com.group3.carrental.entity;
import com.group3.carrental.entity.Vehicule;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class GestionCatalogue {

    public List<Vehicule> rechercherVehicules(List<Vehicule> catalogue, String ville, LocalDate dateRecherchee) {
        return catalogue.stream()
            .filter(v -> v.getVilleLocalisation().equalsIgnoreCase(ville)) 
            .filter(v -> v.getDatesDisponibles().contains(dateRecherchee)) 
            .collect(Collectors.toList());
    }
}