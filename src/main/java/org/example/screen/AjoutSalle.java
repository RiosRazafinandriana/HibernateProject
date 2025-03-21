package org.example.screen;

import org.example.DAO.SalleDAO;
import org.example.entity.Prof;
import org.example.DAO.ProfDAO;
import org.example.entity.Salle;

import javax.swing.*;
import java.awt.*;

public class AjoutSalle {
    private SalleDAO salleDAO;
    private JFrame frame;
    private JTextField codesalleField;
    private JTextField designationField;
    private SalleScreen salleScreen;

    public AjoutSalle(SalleScreen salleScreen) {
        this.salleDAO = new SalleDAO();
        this.salleScreen = salleScreen;
    }

    public void initialize() {
        frame = new JFrame("Ajouter une salle");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 2));  // Modifié pour ajouter une ligne de plus

        // Ajouter des champs de texte pour chaque information
        frame.add(new JLabel("Code de la salle:"));
        codesalleField = new JTextField();
        frame.add(codesalleField);

        frame.add(new JLabel("Désignation :"));
        designationField= new JTextField();
        frame.add(designationField);

        // Ajouter un bouton pour soumettre le formulaire
        JButton submitButton = new JButton("Ajouter");
        submitButton.addActionListener(e -> addSalle());
        frame.add(submitButton);

        // Ajouter un bouton pour annuler l'action
        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> cancelAction());
        frame.add(cancelButton);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void addSalle() {
        // Récupérer les valeurs saisies par l'utilisateur
        String codesalle = codesalleField.getText();
        String designation = designationField.getText();

        // Créer un objet Prof à partir des valeurs saisies
        Salle salle = Salle.builder()
                .codeSal(codesalle)
                .designation(designation)
                .build();

        // Utiliser le DAO pour ajouter le professeur à la base de données
        try {
            salleDAO.addSalle(salle);  // Appel à la méthode pour insérer le professeur

            // Afficher un message de succès
            JOptionPane.showMessageDialog(frame, "Professeur ajouté avec succès !");

            salleScreen.loadSalleData();

            // Réinitialiser les champs de saisie
            resetFields();

            // Fermer la fenêtre
            // Mettre à jour la table après insertion
            frame.dispose();

        } catch (Exception e) {
            // Gérer les exceptions et afficher un message d'erreur
            JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout de la salle : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    // Méthode pour réinitialiser les champs de saisie
    public void cancelAction() {
        int confirmation = JOptionPane.showConfirmDialog(
                frame, "Voulez-vous annuler l'ajout et effacer les champs ?",
                "Annuler l'ajout", JOptionPane.YES_NO_OPTION
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            resetFields();  // Effacer les champs
            frame.dispose();
        }
    }

    // Méthode pour réinitialiser les champs
    private void resetFields() {
        codesalleField.setText("");
        designationField.setText("");
    }
}
