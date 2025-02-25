package org.example.screen;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainFrame() {
        // Configuration de la fenêtre principale
        setTitle("Application avec Barre de Navigation");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Création du gestionnaire de cartes
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Ajout des écrans au CardLayout
        HomeScreen homeScreen = new HomeScreen(this);
        ProfScreen secondScreen = new ProfScreen(this);

        cardPanel.add(homeScreen, "Home");
        cardPanel.add(secondScreen, "Second");

        add(cardPanel);

        // Ajout de la barre de navigation
        setJMenuBar(createMenuBar());
    }

    // Méthode pour afficher un écran spécifique
    public void showScreen(String screenName) {
        cardLayout.show(cardPanel, screenName);
    }

    // Méthode pour créer la barre de menu
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu principal
        JMenu menuNavigation = new JMenu("Navigation");

        // Menu Item - Accueil
        JMenuItem menuHome = new JMenuItem("Accueil");
        menuHome.addActionListener(e -> showScreen("Home"));

        // Menu Item - Écran 2
        JMenuItem menuSecondScreen = new JMenuItem("Écran 2");
        menuSecondScreen.addActionListener(e -> showScreen("Second"));

        // Ajout des items au menu
        menuNavigation.add(menuHome);
        menuNavigation.add(menuSecondScreen);

        // Ajout du menu à la barre de menu
        menuBar.add(menuNavigation);

        return menuBar;
    }
}

