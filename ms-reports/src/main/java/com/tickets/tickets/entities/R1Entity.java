package com.tickets.tickets.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "report1")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R1Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idR1;


    // Using these ids is possible to search for the data of each repair
    @ElementCollection
    private List<Long> vehicleRepairIds;

    // a setter that initializes the list of vehicleRepairIds
    public void setVehicleRepairIdsEmpty(List<Long> vehicleRepairIds) {
        this.vehicleRepairIds = vehicleRepairIds;
    }

}