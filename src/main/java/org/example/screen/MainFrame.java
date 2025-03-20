package org.example.screen;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Map<JButton, Boolean> buttonStates = new HashMap<>();
    private JButton selectedButton = null;

    public MainFrame() {
        setTitle("Hibernate");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UIManager.put("Button.arc", 20);

        // Gestion des cartes
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Ajout des écrans
        SalleScreen salleScreen = new SalleScreen(this);
        ProfScreen profScreen = new ProfScreen(this);
        OccuperScreen occuperScreen = new OccuperScreen(this);

        cardPanel.add(profScreen, "Prof");
        cardPanel.add(salleScreen, "Salle");
        cardPanel.add(occuperScreen, "Occuper");

        add(cardPanel, BorderLayout.CENTER);
        JPanel navBar = createNavBar();
        add(navBar, BorderLayout.NORTH);

        selectButton(navBar);
    }

    public void showScreen(String screenName) {
        cardLayout.show(cardPanel, screenName);
    }

    private JPanel createNavBar() {
        JPanel navBar = new JPanel();
        navBar.setLayout(new BoxLayout(navBar, BoxLayout.X_AXIS));
        navBar.setBackground(new Color(50, 50, 50));
        navBar.setPreferredSize(new Dimension(getWidth(), 50));

        JButton btnProf = createStyledButton("Prof");
        btnProf.addActionListener(e -> showScreen("Prof"));

        JButton btnSalle = createStyledButton("Salle");
        btnSalle.addActionListener(e -> showScreen("Salle"));

        JButton btnOccuper = createStyledButton("Occuper");
        btnOccuper.addActionListener(e -> showScreen("Occuper"));

        // Aligner au centre
        btnProf.setAlignmentY(Component.CENTER_ALIGNMENT);
        btnSalle.setAlignmentY(Component.CENTER_ALIGNMENT);
        btnOccuper.setAlignmentY(Component.CENTER_ALIGNMENT);

        navBar.add(btnProf);
        navBar.add(btnSalle);
        navBar.add(btnOccuper);

        return navBar;
    }

    private JButton createStyledButton(String text) {
        JButton button = createBaseButton(text);
        configureButtonAppearance(button, text);
        button.addMouseListener(new ButtonMouseListener(button)); // Utilisation d'une classe externe
        return button;
    }

    // Crée la structure de base du bouton
    private JButton createBaseButton(String text) {
        return new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                paintStyledBackground(g, this);
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(50, 50, 50));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                g2.dispose();
            }
        };
    }

    // 🎨 Applique les styles et dimensions
    private void configureButtonAppearance(JButton button, String text) {
        buttonStates.put(button, false);
        if (text.equals("Prof") || text.equals("Salle")) {
            button.setMaximumSize(new Dimension(80, 40));
        } else {
            button.setPreferredSize(new Dimension(80, 40));
            button.setMaximumSize(new Dimension(120, 40));
        }
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 50));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
    }

    // 🎨 Dessine le fond du bouton avec l'effet de sélection
    private void paintStyledBackground(Graphics g, JButton button) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boolean isSelected = buttonStates.getOrDefault(button, false);
        g2.setColor(isSelected ? new Color(70, 70, 70) : button.getBackground());
        g2.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 25, 25);
        g2.dispose();
    }

    // 🖱️ Classe séparée pour gérer les événements de la souris
    private class ButtonMouseListener extends MouseAdapter {
        private final JButton button;

        public ButtonMouseListener(JButton button) {
            this.button = button;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!buttonStates.getOrDefault(button, false)) {
                button.setBackground(new Color(70, 70, 70));
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!buttonStates.getOrDefault(button, false)) {
                button.setBackground(new Color(50, 50, 50));
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (selectedButton != null) {
                buttonStates.put(selectedButton, false);
                selectedButton.setBackground(new Color(50, 50, 50));
                selectedButton.repaint();
            }
            buttonStates.put(button, true);
            button.setBackground(new Color(70, 70, 70));
            button.repaint();
            selectedButton = button;
        }
    }


    private void selectButton(JPanel navBar) {
        // Trouver le bouton "Prof" et définir son état à "sélectionné"
        for (Component component : navBar.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText().equals("Prof")) {
                    // Mettre à jour l'état du bouton
                    buttonStates.put(button, true);
                    button.setBackground(new Color(70, 70, 70));
                    selectedButton = button; // Mettre à jour le bouton sélectionné
                    button.repaint(); // Redessiner le bouton
                    break;
                }
            }
        }
    }
}
