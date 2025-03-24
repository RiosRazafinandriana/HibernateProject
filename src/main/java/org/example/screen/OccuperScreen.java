package org.example.screen;

import org.example.DAO.OccuperDAO;
import org.example.DAO.ProfDAO;
import org.example.DAO.SalleDAO;
import org.example.entity.Occuper;
import org.example.entity.Prof;
import org.example.entity.Salle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

public class OccuperScreen extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    public OccuperScreen (MainFrame mainFrame) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240)); // Couleur de fond

        // ---- BOUTON D'AJOUT ----
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.setFont(new Font("Arial", Font.BOLD, 14));
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.setBackground(new Color(50, 150, 250));
        btnAjouter.setFocusPainted(false);
        btnAjouter.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(220, 220, 220));
        topPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        topPanel.add(btnAjouter, BorderLayout.WEST);

        // ---- TABLEAU ----
        String[] columnNames = {"Id","Prof","Salle","Date","Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Seule la colonne Actions est éditable
            }
        };

        // Création du JTable avec le modèle de données
        table = new JTable(tableModel);
        table.setModel(tableModel);
        TableColumn idColumn = table.getColumnModel().getColumn(0);
        table.getColumnModel().removeColumn(idColumn);
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
        table.getColumnModel().getColumn(3).setCellRenderer(new OccuperScreen.ButtonRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new OccuperScreen.ButtonEditor(new JCheckBox()));

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

        loadOccuperData();

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

        // Listener de la souris pour la table
        scrollPane.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                table.clearSelection(); // Désélectionne la ligne sélectionnée
            }
        });


        // Action du bouton d'ajout
        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Créer une nouvelle instance de la fenêtre AjoutProf et l'afficher
                AjoutOccuper ajoutOccuperWindow = new AjoutOccuper(OccuperScreen.this);
                ajoutOccuperWindow.initialize();
            }
        });
    }
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
            int modelRow = table.convertRowIndexToModel(row);
            Occuper occuper = searchOccuperAtRow(modelRow);
            if (occuper != null) {
                ModifOccuper modifierOccuper = new ModifOccuper(OccuperScreen.this, occuper);
                modifierOccuper.initialize();
                System.out.println("Edit button clicked for occuper: " + occuper.getIdOccup());
            }
        });

        deleteButton.addActionListener(e -> {
            isPushed = true;
            fireEditingStopped();
            // Use modelRow consistently
            int modelRow = table.convertRowIndexToModel(row);
            Occuper occuper = searchOccuperAtRow(modelRow);
            if (occuper != null) {
                int result = JOptionPane.showConfirmDialog(
                        panel,
                        "Êtes-vous sûr de vouloir supprimer la ligne: " + occuper.getIdOccup(),
                        "Confirmation de suppression",
                        JOptionPane.YES_NO_OPTION
                );
                if (result == JOptionPane.YES_OPTION) {
                    OccuperDAO occuperDAO = new OccuperDAO();
                    occuperDAO.deleteOccuper(occuper.getIdOccup());
                    loadOccuperData();
                    System.out.println("Delete button clicked for occup: " + occuper.getIdOccup());
                }else {
                    loadOccuperData();
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
    private Occuper searchOccuperAtRow(int modelRow) {
        if (modelRow >= 0 && modelRow < tableModel.getRowCount()) {
            int idOccup = (Integer) tableModel.getValueAt(modelRow, 0);
            String prof = (String) tableModel.getValueAt(modelRow, 1);
            String classe = (String) tableModel.getValueAt(modelRow, 2);
            LocalDate date = (LocalDate) tableModel.getValueAt(modelRow, 3);

            SalleDAO salleDAO = new SalleDAO();
            Salle classeEntity = salleDAO.getSalle(classe);
            ProfDAO profDAO = new ProfDAO();
            Prof profEntity = profDAO.getProf(prof);

            // Create and return a Occuper object with these values
            return new Occuper(idOccup, profEntity, classeEntity, date);
        }
        return null;
    }
    public void loadOccuperData() {
        OccuperDAO occuperDAO = new OccuperDAO();
        List<Occuper> occuperList = occuperDAO.getAllOccuperList();
        table.setRowSorter(null);
        tableModel.setRowCount(0);

        for (Occuper occuper : occuperList) {
            Object[] rowData = new Object[5]; // Change to 5 columns to include the Actions column
            rowData[0] = occuper.getIdOccup();
            rowData[1] = occuper.getProf().getCodeprof();
            rowData[2] = occuper.getSalle().getCodeSal();
            rowData[3] = occuper.getDate();
            rowData[4] = ""; // This will be replaced by the buttons
            tableModel.addRow(rowData);
        }
    }
}
