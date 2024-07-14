package com.tickets.tickets.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "month_repair")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthRepairEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idRepair;

    private int repairType;

    // This entity represents the repair row of data and
    // there are 12 types, not more nor less


    // result = [[repairType, amount, price, month]]
    // 3 months
    // represented as ints
    private int firstMonth;  // Previous of previous
    private int secondMonth; // Previous
    private int thirdMonth; // Current

    // First month data
    private int firstMonthAmount;
    private int firstMonthPrice;

    // Second month data
    private int secondMonthAmount;
    private int secondMonthPrice;

    // Third month data
    private int thirdMonthAmount;
    private int thirdMonthPrice;

    // Variations
    // Between first and third month
    private int firstAmountVariation;
    private int firstPriceVariation;

    // Between second and third month
    private int secondAmountVariation;
    private int secondPriceVariation;

}