package com.tickets.tickets.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Vehicle {
    // license plate
    private Long licensePlate;

    // These values are important to calculate surcharges and discounts
    private String brand;
    private String model;
    private int vehicleType;
    private int mileage;
    private int year;
    private int engineType;
    private int seats;
}
