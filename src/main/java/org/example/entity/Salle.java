package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "Salle")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salle {
    @Id
    private String codeSal;
    private String designation;
}
