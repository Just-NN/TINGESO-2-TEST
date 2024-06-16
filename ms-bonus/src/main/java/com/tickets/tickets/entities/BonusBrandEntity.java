package usach.tingeso.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bonusbrand")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonusBrandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idBonus;

    private Long idTicket;

    private String brand;
    private int bonus;
    private Boolean active;
}