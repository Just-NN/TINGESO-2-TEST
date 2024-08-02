package com.tickets.tickets.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "ticket")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idTicket;

    // Using this value I can verify that the repairs are related to this ticket
    Date pickupDate;

    // Need the dates to calculate the time
    private Date entryDate;
    private Date exitDate;
    private LocalTime exitTime;
    private LocalTime pickupTime;

    // The total time for the repair
    private Long totalRepairAmount;

    // Vehicle plate to link the ticket with the vehicle
    private Long licensePlate;

    // List of repairs related to the ticket using the ids of the repair
    @ElementCollection
    private List<Integer> typeOfRepairs;

    // Other fields related to prices and discounts
    private double surchargeForKM;
    private double surchargeForAge;
    private double surchargeForDelay;
    private int discountForRepairs;
    private double discountPerDay;
    private double discountForBonus;
    private double brandBonus;

    // Base price of the ticket
    private int basePrice;

    // Total price of the ticket
    private int totalPrice;

    //--------------------------------------------------
    // Here goes the attributes for my HU4
    // Data from vehicle
    private String brand;
    private String model;
    private int engineType;
    private int vehicleType;
    private int year;
    // Data from repair
    private int totalSurcharges;
    private int totalDiscounts;
    private int subTotal;
    // Calculated value
    private int ivaValue;


    // I'll need the methods to calculate the time
    public LocalTime getEntryTime(){
        if (this.entryDate == null) {
            // return a default value or throw an exception
            return null;
        }
        ZonedDateTime zdt = this.entryDate.toInstant().atZone(ZoneId.systemDefault());
        return zdt.toLocalTime();
    }
    // idem but for exit
    public LocalTime getExitTime(){
        if (this.exitDate == null) {
            // return a default value or throw an exception
            return null;
        }
        ZonedDateTime zdt = this.exitDate.toInstant().atZone(ZoneId.systemDefault());
        return zdt.toLocalTime();
    }
    // idem but for pickup
    public LocalTime getPickupTime(){
        if (this.pickupDate == null) {
            // return a default value or throw an exception
            return null;
        }
        ZonedDateTime zdt = this.pickupDate.toInstant().atZone(ZoneId.systemDefault());
        return zdt.toLocalTime();
    }

    public void calculateDifferenceInDays() {
        if (this.entryDate == null || this.exitDate == null) {
            // Handle null entryDate or exitDate as per your application's requirements
            return;
        }
        long diffInMillies = Math.abs(this.exitDate.getTime() - this.entryDate.getTime());
        this.totalRepairAmount = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}