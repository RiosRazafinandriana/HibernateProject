package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "Prof")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prof {
    @Id
    private String codeprof;
    private String nom;
    private String prenom;
    private String grade;
}
