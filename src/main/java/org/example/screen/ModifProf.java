package org.example.screen;

import org.example.DAO.ProfDAO;
import org.example.entity.Prof;

import javax.swing.*;
import java.awt.*;

public class ModifProf {
    private ProfDAO profDAO;
    private JFrame frame;
    private JTextField codeprofField;
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField gradeField;
    private ProfScreen profScreen;
    private Prof prof;

    public ModifProf(ProfScreen profScreen, Prof prof) {
        this.profDAO = new ProfDAO();
        this.profScreen = profScreen;
        this.prof = prof;
    }

    public void initialize() {
        frame = new JFrame("Modifier un Professeur");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 2));  // Modifié pour ajouter une ligne de plus

        // Ajouter des champs de texte pour chaque information
        //frame.add(new JLabel("Code du professeur:"));
        codeprofField = new JTextField();
        codeprofField.setText(prof.getCodeprof());
        frame.add(codeprofField);
        frame.remove(codeprofField);
        frame.revalidate();
        frame.repaint();

        frame.add(new JLabel("Nom du professeur:"));
        nomField = new JTextField();
        nomField.setText(prof.getNom());
        frame.add(nomField);

        frame.add(new JLabel("Prénom du professeur:"));
        prenomField = new JTextField();
        prenomField.setText(prof.getPrenom());
        frame.add(prenomField);

        frame.add(new JLabel("Grade du professeur:"));
        gradeField = new JTextField();
        gradeField.setText(prof.getGrade());
        frame.add(gradeField);

        // Ajouter un bouton pour soumettre le formulaire
        JButton submitButton = new JButton("Modifier");
        submitButton.addActionListener(e -> modifProf());
        frame.add(submitButton);

        // Ajouter un bouton pour annuler l'action
        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> cancelAction());
        frame.add(cancelButton);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void modifProf() {
        // Récupérer les valeurs saisies par l'utilisateur
        String codeprof = codeprofField.getText();
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String grade = gradeField.getText();

        // Créer un objet Prof à partir des valeurs saisies
        Prof prof = Prof.builder()
                .codeprof(codeprof)
                .nom(nom)
                .prenom(prenom)
                .grade(grade)
                .build();

        // Utiliser le DAO pour ajouter le professeur à la base de données
        try {
            profDAO.updateProf(prof);  // Appel à la méthode pour insérer le professeur

            // Afficher un message de succès
            JOptionPane.showMessageDialog(frame, "Professeur modifié avec succès !");

            // Mettre à jour la table après insertion
            profScreen.loadProfData();

            // Réinitialiser les champs de saisie
            resetFields();

            // Fermer la fenêtre
            frame.dispose();

        } catch (Exception e) {
            // Gérer les exceptions et afficher un message d'erreur
            JOptionPane.showMessageDialog(frame, "Erreur lors du mise à jour du professeur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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
        codeprofField.setText("");
        nomField.setText("");
        prenomField.setText("");
        gradeField.setText("");
    }
}

