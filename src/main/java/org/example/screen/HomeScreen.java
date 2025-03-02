package org.example.screen;

import javax.swing.*;
import java.awt.*;

public class HomeScreen extends JPanel {
    public HomeScreen(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // Ajout du titre
        JLabel titleLabel = new JLabel("Bienvenue dans l'application !", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Création du bouton
        JButton startButton = new JButton("Démarrer");
        startButton.setFont(new Font("Arial", Font.PLAIN, 16));

        // Action pour naviguer vers le SecondScreen
        startButton.addActionListener(e -> mainFrame.showScreen("Second"));

        add(startButton, BorderLayout.CENTER);
    }
}
