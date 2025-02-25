package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Entity
@Table(name = "Occuper")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Occuper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idOccup;

    @ManyToOne
    @JoinColumn(name = "codeprof", referencedColumnName = "codeprof")
    private Prof prof;

    @ManyToOne
    @JoinColumn(name = "codesal", referencedColumnName = "codesal")
    private Salle salle;

    private LocalDate date;
}
