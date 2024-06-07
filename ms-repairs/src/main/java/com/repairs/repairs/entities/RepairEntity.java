package com.repairs.repairs.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "repair")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idRepair;

    private Long idTicket;


    private int repairType;


    private int kmSurcharge;
    private int ageSurcharge;
    private int delaySurcharge;

    private int dayDiscount;
    private int repairsDiscount;

    private int basePrice;
    private int totalPrice;



}