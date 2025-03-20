package org.example.screen;

import javax.swing.*;
import java.awt.*;

public class SalleScreen extends JPanel {
    public SalleScreen(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // Ajout du titre
        JLabel titleLabel = new JLabel("Liste des salles", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Création du bouton
        JButton startButton = new JButton("Démarrer");
        startButton.setFont(new Font("Arial", Font.PLAIN, 16));

        // Action pour naviguer vers le SecondScreen
        startButton.addActionListener(e -> mainFrame.showScreen("Salle"));

        add(startButton, BorderLayout.CENTER);
    }
}
