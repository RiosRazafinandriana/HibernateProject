package org.example.screen;

import org.example.DAO.SalleDAO;
import org.example.entity.Salle;

import javax.swing.*;
import java.awt.*;

public class ModifSalle {
    private SalleDAO salleDAO;
    private JFrame frame;
    private JTextField codesalleField;
    private JTextField designationField;
    private SalleScreen salleScreen;
    private Salle salle;

    public ModifSalle(SalleScreen salleScreen, Salle salle) {
        this.salleDAO = new SalleDAO();
        this.salleScreen = salleScreen;
        this.salle = salle;
    }

    public void initialize() {
        frame = new JFrame("Modifier un Salleesseur");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 2));  // Modifié pour ajouter une ligne de plus

        // Ajouter des champs de texte pour chaque information
        //frame.add(new JLabel("Code du salleesseur:"));
        codesalleField = new JTextField();
        codesalleField.setText(salle.getCodeSal());
        frame.add(codesalleField);
        frame.remove(codesalleField);
        frame.revalidate();
        frame.repaint();

        frame.add(new JLabel("Désignation:"));
        designationField = new JTextField();
        designationField.setText(salle.getDesignation());
        frame.add(designationField);

        // Ajouter un bouton pour soumettre le formulaire
        JButton submitButton = new JButton("Modifier");
        submitButton.addActionListener(e -> modifSalle());
        frame.add(submitButton);

        // Ajouter un bouton pour annuler l'action
        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> cancelAction());
        frame.add(cancelButton);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void modifSalle() {
        // Récupérer les valeurs saisies par l'utilisateur
        String codesalle = codesalleField.getText();
        String designation = designationField.getText();

        // Créer un objet Salle à partir des valeurs saisies
        Salle salle = Salle.builder()
                .codeSal(codesalle)
                .designation(designation)
                .build();

        // Utiliser le DAO pour ajouter le salleesseur à la base de données
        try {
            salleDAO.updateSalle(salle);  // Appel à la méthode pour insérer le salleesseur

            // Afficher un message de succès
            JOptionPane.showMessageDialog(frame, "Salle modifiée avec succès !");

            // Mettre à jour la table après insertion
            salleScreen.loadSalleData();

            // Réinitialiser les champs de saisie
            resetFields();

            // Fermer la fenêtre
            frame.dispose();

        } catch (Exception e) {
            // Gérer les exceptions et afficher un message d'erreur
            JOptionPane.showMessageDialog(frame, "Erreur lors du mise à jour de la salle : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    // Méthode pour réinitialiser les champs de saisie
    public void cancelAction() {
        int confirmation = JOptionPane.showConfirmDialog(
                frame, "Voulez-vous annuler la mise à jour et effacer les champs ?",
                "Annuler la mise à jour", JOptionPane.YES_NO_OPTION
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

