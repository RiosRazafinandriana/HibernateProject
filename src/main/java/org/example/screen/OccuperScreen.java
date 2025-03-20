package org.example.screen;

import javax.swing.*;
import java.awt.*;

public class OccuperScreen extends JPanel {
    public OccuperScreen (MainFrame mainFrame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Créer une contrainte pour le label
        gbc.gridx = 0; // Première colonne
        gbc.gridy = 0; // Première ligne
        gbc.anchor = GridBagConstraints.PAGE_END;  // Aligner en bas
        add(new JLabel("Liste des Occupations", JLabel.CENTER), gbc);

        // Ajouter un espace entre le label et le bouton
        gbc.gridy = 1;
        add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        // Créer une contrainte pour le bouton
        gbc.gridy = 2; // Positionner le bouton plus bas
        JButton backButton = new JButton("Retour");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(Color.YELLOW);
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(150, 50));

        backButton.setOpaque(true);
        backButton.setBorderPainted(false);

        // Action pour revenir à HomeScreen
        backButton.addActionListener(e -> mainFrame.showScreen("Prof"));
        add(backButton, gbc);
    }
}
