package org.example.screen;

import org.example.entity.Prof;
import org.example.DAO.ProfDAO;

import javax.swing.*;
import java.awt.*;

public class AjoutProf {
    private ProfDAO profDAO;
    private JFrame frame;
    private JTextField codeprofField;
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField gradeField;
    private ProfScreen profScreen;

    public AjoutProf(ProfScreen profScreen) {
        this.profDAO = new ProfDAO();
        this.profScreen = profScreen;
    }

    public void initialize() {
        frame = new JFrame("Ajouter un Professeur");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new GridLayout(4, 2));  // Modifié pour ajouter une ligne de plus

        // Ajouter des champs de texte pour chaque information
//        frame.add(new JLabel("Code du professeur:"));
//        codeprofField = new JTextField();
//        frame.add(codeprofField);

        frame.add(new JLabel("Nom du professeur:"));
        nomField = new JTextField();
        frame.add(nomField);

        frame.add(new JLabel("Prénom du professeur:"));
        prenomField = new JTextField();
        frame.add(prenomField);

        frame.add(new JLabel("Grade du professeur:"));
        gradeField = new JTextField();
        frame.add(gradeField);

        // Ajouter un bouton pour soumettre le formulaire
        JButton submitButton = new JButton("Ajouter");
        submitButton.addActionListener(e -> addProf());
        frame.add(submitButton);

        // Ajouter un bouton pour annuler l'action
        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> cancelAction());
        frame.add(cancelButton);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void addProf() {
        // Récupérer les valeurs saisies par l'utilisateur
        //String codeprof = codeprofField.getText();
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String grade = gradeField.getText();

        String lastCode = profDAO.findLastCodeProf();
        System.out.println(lastCode);
        int nextNumber = 1;

        if (lastCode != null && lastCode.startsWith("p")) {
            nextNumber = Integer.parseInt(lastCode.substring(1)) + 1;
        }

        String newCodeProf = "p" + nextNumber;

        // Créer un objet Prof à partir des valeurs saisies
        Prof prof = Prof.builder()
                .codeprof(newCodeProf)
                .nom(nom)
                .prenom(prenom)
                .grade(grade)
                .build();

        // Utiliser le DAO pour ajouter le professeur à la base de données
        try {
            profDAO.addProf(prof);  // Appel à la méthode pour insérer le professeur

            // Afficher un message de succès
            JOptionPane.showMessageDialog(frame, "Professeur ajouté avec succès !");

            // Mettre à jour la table après insertion
            profScreen.loadProfData();

            // Réinitialiser les champs de saisie
            resetFields();

            // Fermer la fenêtre
            frame.dispose();

        } catch (Exception e) {
            // Gérer les exceptions et afficher un message d'erreur
            JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout du professeur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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
        //codeprofField.setText("");
        nomField.setText("");
        prenomField.setText("");
        gradeField.setText("");
    }
}
