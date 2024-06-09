package com.tickets.tickets.models;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    private Long licensePlate;
    private String brand;
    private String model;
    private int vehicleType;
    private int mileage;
    private int year;
    private int engineType;
    private int seats;
}