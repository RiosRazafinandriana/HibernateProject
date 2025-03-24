package org.example.screen;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import org.example.DAO.OccuperDAO;
import org.example.DAO.SalleDAO;
import org.example.entity.Occuper;
import org.example.entity.Prof;
import org.example.DAO.ProfDAO;
import org.example.entity.Salle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class ModifOccuper {
    private OccuperDAO occuperDAO;
    private SalleDAO salleDAO;
    private ProfDAO profDAO;
    private JFrame frame;
    private JComboBox<String> comboSalleBox;
    private JComboBox<String> comboProfBox;
    private Salle salleSelected;
    private Prof profSelected ;
    private DatePicker datePicker;
    private LocalDate dateOccup;
    private OccuperScreen occuperScreen;
    private Occuper occuper;

    public ModifOccuper(OccuperScreen occuperScreen, Occuper occuper) {
        this.occuperDAO = new OccuperDAO();
        this.salleDAO = new SalleDAO();
        this.profDAO = new ProfDAO();
        this.occuperScreen = occuperScreen;
        this.occuper = occuper;
    }

    public void initialize() {
        frame = new JFrame("Modification");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(4, 2));

        //Prof selector
        frame.add(new JLabel("Professeur:"));
        List<Prof> profs = profDAO.getAllProfs();
        String[] profsOptions = new String[profs.size()];
        for (int i = 0; i < profs.size(); i++) {
            profsOptions[i] = profs.get(i).getCodeprof();
        }
        comboProfBox = new JComboBox<>(profsOptions);
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < profsOptions.length; i++) {
                if(profsOptions[i].equals(occuper.getProf().getCodeprof())) {
                    comboProfBox.setSelectedIndex(i); // Utiliser setSelectedIndex au lieu de setSelectedItem
                    break;
                }
            }
        });
        comboProfBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> source = (JComboBox<String>)e.getSource();
                String itemSelectionne = (String)source.getSelectedItem();
                profSelected = profDAO.getProf(itemSelectionne);
            }
        });
        frame.add(comboProfBox);

        // Salle selector
        frame.add(new JLabel("Salle:"));
        List<Salle> salles = salleDAO.getAllSalles();
        String[] sallesOptions = new String[salles.size()];;
        for (int i = 0; i < salles.size(); i++) {
            sallesOptions[i] = salles.get(i).getCodeSal();
        }
        comboSalleBox = new JComboBox<>(sallesOptions);
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < sallesOptions.length; i++) {
                if(sallesOptions[i].equals(occuper.getSalle().getCodeSal()) ){
                    comboSalleBox.setSelectedIndex(i);
                    break;
                }
            }
        });
        comboSalleBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> source = (JComboBox<String>)e.getSource();
                String itemSelectionne = (String)source.getSelectedItem();
                salleSelected = salleDAO.getSalle(itemSelectionne);
                System.out.println("Item sélectionné: " + itemSelectionne);
            }
        });
        frame.add(comboSalleBox);

        //DatePicker
        frame.add(new JLabel("Date: "));
        DatePickerSettings dateSettings = new DatePickerSettings(Locale.FRANCE);
        dateSettings.setAllowEmptyDates(false);

        datePicker = new DatePicker(dateSettings);
        datePicker.setDate(occuper.getDate());

        datePicker.addDateChangeListener(event -> {
            dateOccup = event.getNewDate();
            System.out.println("Date sélectionnée: " + dateOccup);
        });
        frame.add(datePicker);

        // Ajouter un bouton pour soumettre le formulaire
        JButton submitButton = new JButton("Ajouter");
        submitButton.addActionListener(e -> modifOccuper());
        frame.add(submitButton);

        // Ajouter un bouton pour annuler l'action
        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> cancelAction());
        frame.add(cancelButton);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void modifOccuper() {
        if (dateOccup == null) {
            dateOccup = datePicker.getDate();
        }
        Occuper occuper1 = Occuper.builder()
                .idOccup(occuper.getIdOccup())
                .salle(salleSelected)
                .prof(profSelected)
                .date(dateOccup)
                .build();

        try {
            occuperDAO.updateOccuper(occuper1);

            JOptionPane.showMessageDialog(frame, "Modifié avec succès !");

            occuperScreen.loadOccuperData();

            // Réinitialiser les champs de saisie


            // Fermer la fenêtre
            // Mettre à jour la table après insertion
            frame.dispose();

        } catch (Exception e) {
            // Gérer les exceptions et afficher un message d'erreur
            JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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
            frame.dispose();
            occuperScreen.loadOccuperData();
        }
    }

    // Méthode pour réinitialiser les champs
}
