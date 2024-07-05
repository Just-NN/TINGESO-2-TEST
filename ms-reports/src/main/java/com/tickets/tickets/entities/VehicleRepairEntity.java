package com.tickets.tickets.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicle_repair")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRepairEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idRepair;

    private int repairType;

    // This entity represents the repair row of data and
    // there are 12 types, not more nor less


    // 5 types of vehicles
    // Amount of repairs per type of vehicle
    private int sedanAmount;
    private int hatchbackAmount;
    private int suvAmount;
    private int pickupAmount;
    private int truckAmount;


    // Price per type of vehicle
    private int sedanPrice;
    private int hatchbackPrice;
    private int suvPrice;
    private int pickupPrice;
    private int truckPrice;


    // Totals
    private int totalAmount;
    private int totalPrice;

}