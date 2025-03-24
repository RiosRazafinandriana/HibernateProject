package org.example.screen;

import org.example.DAO.ProfDAO;
import org.example.entity.Prof;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ProfScreen extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private CustomTextField searchBar;

    public ProfScreen(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240)); // Couleur de fond

        // ---- BOUTON D'AJOUT ----
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.setFont(new Font("Arial", Font.BOLD, 14));
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.setBackground(new Color(50, 150, 250));
        btnAjouter.setFocusPainted(false);
        btnAjouter.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        searchBar = new CustomTextField("Rechercher un professeur");
        searchBar.setPreferredSize(new Dimension(200,30));
        searchBar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTableContent();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTableContent();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTableContent();
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(220, 220, 220));
        topPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        topPanel.add(btnAjouter, BorderLayout.WEST);
        topPanel.add(searchBar, BorderLayout.EAST);

        // ---- TABLEAU ----
        String[] columnNames = {"Code", "Nom", "Prénom", "Grade", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Seule la colonne Actions est éditable
            }
        };

        // Création du JTable avec le modèle de données
        table = new JTable(tableModel);
        table.setModel(tableModel);
        table.setDefaultEditor(Object.class, null);

        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(50, 50, 50));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        table.setFocusable(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(false);  // Désactive la sélection des cellules
        table.setColumnSelectionAllowed(false); // Désactive la sélection des colonnes
        table.setAutoscrolls(false);  // Désactive le défilement automatique
        table.setRequestFocusEnabled(false); // Désactive le focus sur les cellules
        table.setRowSelectionAllowed(true);

        // Personnaliser le rendu pour ne pas afficher de bordure autour des cellules sélectionnées
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                this.setHorizontalAlignment(JLabel.CENTER);

                if (isSelected) {
                    c.setBackground(new Color(100, 150, 250)); // Bleu clair pour la sélection
                    c.setForeground(Color.WHITE); // Texte en blanc
                } else {
                    if (row % 2 != 0) {
                        c.setBackground(Color.WHITE); // Blanc pour les lignes paires
                    } else {
                        c.setBackground(new Color(230, 230, 230)); // Gris clair pour les lignes impaires
                    }
                    c.setForeground(Color.BLACK); // Texte noir pour une bonne lisibilité
                }

                return c;
            }
        });

        // Set up the custom renderer for the Actions column
        table.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Vérifie si c'est un double-clic
                    e.consume(); // Bloque l'événement pour éviter l'édition
                }
            }
        });

        // ---- SCROLLPANE POUR LE TABLEAU ----
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        loadProfData();

        // ---- AJOUT DES COMPOSANTS À L'INTERFACE ----
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Crée un FocusListener pour écouter les événements de focus
        FocusListener focusListener = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // Quand le JTextField reçoit le focus, rien de spécial à faire ici
                table.clearSelection();
            }

            @Override
            public void focusLost(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    table.clearSelection();
                });
            }
        };

        // Ajoute le FocusListener au JTextField
        searchBar.addFocusListener(focusListener);

        // Listener de la souris pour la table
        scrollPane.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                table.clearSelection(); // Désélectionne la ligne sélectionnée

                // Quand on clique ailleurs, on veut s'assurer que le focus ne retourne pas au JTextField
                if (searchBar.hasFocus()) {
                    // Retirer le focus du JTextField et l'envoyer ailleurs
                    searchBar.transferFocus(); // Transférer le focus
                    table.clearSelection();
                }

                JPanel dummyPanel = new JPanel(); // Neutre, non interactif
                dummyPanel.requestFocusInWindow();
            }
        });


        // Action du bouton d'ajout
        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Créer une nouvelle instance de la fenêtre AjoutProf et l'afficher
                AjoutProf ajoutProfWindow = new AjoutProf(ProfScreen.this);
                ajoutProfWindow.initialize();  // Ouvrir la fenêtre AjoutProf
            }
        });
    }

    // Custom renderer for the buttons
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton editButton;
        private JButton deleteButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setOpaque(true);

            // Create Edit button
            editButton = new JButton("Modifier");
            editButton.setPreferredSize(new Dimension(80, 25));
            editButton.setFont(new Font("Arial", Font.PLAIN, 12));
            editButton.setBackground(new Color(92, 184, 92));
            editButton.setForeground(Color.WHITE);
            editButton.setFocusPainted(false);
            editButton.setBorderPainted(false);
            add(editButton);

            // Create Delete button
            deleteButton = new JButton("Supprimer");
            deleteButton.setPreferredSize(new Dimension(80, 25));
            deleteButton.setFont(new Font("Arial", Font.PLAIN, 12));
            deleteButton.setBackground(new Color(217, 83, 79));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setFocusPainted(false);
            deleteButton.setBorderPainted(false);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(new Color(100, 150, 250));
            } else {
                if (row % 2 != 0) {
                    setBackground(Color.WHITE);
                } else {
                    setBackground(new Color(230, 230, 230));
                }
            }
            return this;
        }
    }

    // Custom editor for the buttons
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton editButton;
        private JButton deleteButton;
        private String label;
        private boolean isPushed;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);

            panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

            // Create Edit button
            editButton = new JButton("Modifier");
            editButton.setPreferredSize(new Dimension(80, 25));
            editButton.setFont(new Font("Arial", Font.PLAIN, 12));
            editButton.setBackground(new Color(92, 184, 92));
            editButton.setForeground(Color.WHITE);
            editButton.setFocusPainted(false);
            editButton.setBorderPainted(false);

            // Create Delete button
            deleteButton = new JButton("Supprimer");
            deleteButton.setPreferredSize(new Dimension(80, 25));
            deleteButton.setFont(new Font("Arial", Font.PLAIN, 12));
            deleteButton.setBackground(new Color(217, 83, 79));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setFocusPainted(false);
            deleteButton.setBorderPainted(false);

            panel.add(editButton);
            panel.add(deleteButton);

            // Add action listeners to the buttons
            editButton.addActionListener(e -> {
                isPushed = true;
                fireEditingStopped();
                // Code to handle edit action
                int modelRow = table.convertRowIndexToModel(row);

                // Utilisez modelRow au lieu de row
                Prof prof = searchProfAtRow(modelRow);
                if (prof != null) {
                    // Example: Open edit dialog
                    ModifProf modifierProf = new ModifProf(ProfScreen.this, prof);
                    modifierProf.initialize();
                    System.out.println("Edit button clicked for prof: " + prof.getCodeprof());
                }
            });

            deleteButton.addActionListener(e -> {
                isPushed = true;
                fireEditingStopped();
                // Code to handle delete action
                int modelRow = table.convertRowIndexToModel(row);
                Prof prof = searchProfAtRow(row);
                if (prof != null) {
                    // Example: Confirm deletion
                    int result = JOptionPane.showConfirmDialog(
                            panel,
                            "Êtes-vous sûr de vouloir supprimer ce professeur ?",
                            "Confirmation de suppression",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (result == JOptionPane.YES_OPTION) {
                        // Delete the professor
                        ProfDAO profDAO = new ProfDAO();
                        profDAO.deleteProf(prof.getCodeprof());
                        loadProfData();  // Refresh the table
                        System.out.println("Delete button clicked for prof: " + prof.getCodeprof());
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            if (isSelected) {
                panel.setBackground(new Color(100, 150, 250));
            } else {
                if (row % 2 != 0) {
                    panel.setBackground(Color.WHITE);
                } else {
                    panel.setBackground(new Color(230, 230, 230));
                }
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    private Prof searchProfAtRow(int modelRow) {
        // À ce stade, modelRow est déjà l'index du modèle, donc pas besoin de conversion supplémentaire
        String code = (String) tableModel.getValueAt(modelRow, 0);
        String nom = (String) tableModel.getValueAt(modelRow, 1);
        String prenom = (String) tableModel.getValueAt(modelRow, 2);
        String grade = (String) tableModel.getValueAt(modelRow, 3);

        // Créer et retourner un objet Prof avec ces valeurs
        return new Prof(code, nom, prenom, grade);
    }

    // Helper method to get Prof object at a specific row
    private Prof getProfAtRow(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            String codeProf = (String) tableModel.getValueAt(row, 0);
            ProfDAO profDAO = new ProfDAO();
            return profDAO.getProf(codeProf);
        }
        return null;
    }

    public void loadProfData() {
        ProfDAO profDAO = new ProfDAO();
        List<Prof> profs = profDAO.getAllProfs();
        table.setRowSorter(null);
        tableModel.setRowCount(0);

        for (Prof prof : profs) {
            Object[] rowData = new Object[5]; // Change to 5 columns to include the Actions column
            rowData[0] = prof.getCodeprof();
            rowData[1] = prof.getNom();
            rowData[2] = prof.getPrenom();
            rowData[3] = prof.getGrade();
            rowData[4] = ""; // This will be replaced by the buttons
            tableModel.addRow(rowData);
        }
    }

    private void filterTableContent() {
        String searchText = searchBar.getText().toLowerCase();

        // Si le texte est le placeholder ou vide, afficher toutes les données
        if (searchText.equals("Rechercher un professeur") || searchText.isEmpty()) {
            loadProfData(); // Recharge toutes les données
            return;
        }

        // Créer un nouveau TableRowSorter pour le filtrage
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Définir le filtre qui vérifie si le texte de recherche est contenu dans les colonnes 0, 1, 2 ou 3
        RowFilter<DefaultTableModel, Object> rf = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                // Vérifier les colonnes Code, Nom, Prénom et Grade (index 0 à 3)
                String codeValue = entry.getStringValue(0).toLowerCase();
                if (codeValue.contains(searchText)) {
                    return true;
                }

                // Vérifier seulement la colonne Nom (index 1)
                String nomValue = entry.getStringValue(1).toLowerCase();
                if (nomValue.contains(searchText)) {
                    return true;
                }
                return false;
            }
        };

        sorter.setRowFilter(rf);
    }
}
class CustomTextField extends JTextField {
        private String placeholder;

    public CustomTextField(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        if (getText().isEmpty() && !hasFocus()) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.GRAY);
            g2.drawString(placeholder, getInsets().left, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 2);
        }
    }
}