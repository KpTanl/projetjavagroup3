package com.group3.carrental.entity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

@Entity
@Data
@NoArgsConstructor
public class Contrat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateDeb;
    private Date dateFin;
    @ManyToOne
    private Agent agent;
    @ManyToOne
    private Loueur loueur;
    @ManyToOne
    private Vehicule vehicule;
    private double prixTotal;

    @Enumerated(EnumType.STRING)
    private Statut statut;

    private String cheminPhotoKilometrage;

    public enum Statut {
        Presigne,
        Accepte,
        Refuse,
        Rendu
    }

    public Contrat(Date dateDeb, Date dateFin, Agent agent, Loueur loueur, Vehicule vehicule, double prixTotal) {
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
        this.agent = agent;
        this.loueur = loueur;
        this.vehicule = vehicule;
        this.prixTotal = prixTotal;
    }

    public String genererPdf(String dossierDestination) throws IOException {
        // Créer le dossier s'il n'existe pas
        File dossier = new File(dossierDestination);
        if (!dossier.exists()) {
            boolean created = dossier.mkdirs();
            if (!created) {
                throw new IOException("Impossible de créer le dossier: " + dossierDestination);
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String nomFichier = dossierDestination + "/contrat_" + id + ".pdf";

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Titre
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 20);
                contentStream.newLineAtOffset(200, 750);
                contentStream.showText("CONTRAT DE LOCATION");
                contentStream.endText();

                // Informations du contrat
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.setLeading(18f);
                contentStream.newLineAtOffset(50, 700);

                contentStream.showText("Contrat N° " + id);
                contentStream.newLine();
                contentStream.newLine();

                // Agent
                contentStream.showText("AGENT:");
                contentStream.newLine();
                contentStream.showText("  Nom: " + agent.getNom() + " " + agent.getPrenom());
                contentStream.newLine();
                contentStream.showText("  Email: " + agent.getEmail());
                contentStream.newLine();
                contentStream.newLine();

                // Loueur
                contentStream.showText("LOUEUR:");
                contentStream.newLine();
                contentStream.showText("  Nom: " + loueur.getNom() + " " + loueur.getPrenom());
                contentStream.newLine();
                contentStream.showText("  Email: " + loueur.getEmail());
                contentStream.newLine();
                contentStream.newLine();

                // Véhicule
                contentStream.showText("VEHICULE:");
                contentStream.newLine();
                contentStream.showText("  Type: " + vehicule.getType());
                contentStream.newLine();
                contentStream.showText("  Marque: " + vehicule.getMarque() + " " + vehicule.getModele());
                contentStream.newLine();
                contentStream.showText("  Couleur: " + vehicule.getCouleur());
                contentStream.newLine();
                contentStream.showText("  Localisation: " + vehicule.getLocalisationComplete());
                contentStream.newLine();
                contentStream.newLine();

                // Dates
                contentStream.showText("PERIODE DE LOCATION:");
                contentStream.newLine();
                contentStream.showText("  Date de debut: " + sdf.format(dateDeb));
                contentStream.newLine();
                contentStream.showText("  Date de fin: " + sdf.format(dateFin));
                contentStream.newLine();
                contentStream.newLine();

                // Prix
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
                contentStream.showText("PRIX TOTAL: " + prixTotal + " EUR");
                contentStream.newLine();
                contentStream.newLine();

                // Statut
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.showText("Statut: " + (statut != null ? statut : "Non defini"));
                contentStream.newLine();
                contentStream.newLine();

                // Signatures
                contentStream.newLine();
                contentStream.newLine();
                contentStream
                        .showText("Signature Agent: ___________________     Signature Loueur: ___________________");

                contentStream.endText();
            }

            document.save(nomFichier);
        }

        return nomFichier;
    }

    public class StatutContrat {
    }

    public boolean estTermine() {
        if (this.dateFin == null)
            return false;
        return this.dateFin.before(new Date());
    }

    public boolean estAccepte() {
        return this.statut == Statut.Accepte;
    }

    public double calculerPrixAjuste() {
        double prixCible = this.prixTotal;

        if (vehicule.getOptionRetour() == Vehicule.OptionRetour.retour_parking &&
                vehicule.getParkingPartenaire() != null) {
            double reduction = vehicule.getParkingPartenaire().getReductionloueur();
            prixCible = prixCible * (1 - reduction);
        }

        return Math.round(prixCible * 100.0) / 100.0;
    }

}
