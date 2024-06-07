package com.tickets.tickets.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Repair {
    // The id of the repair
    private Long idRepair;

    // Each ticket is linked to many repairs so these must have the idticket
    private Long idTicket;

    // The id of the repair
    private int repairType;

    // Calculated partial Surcharges
    private int kmSurcharge;
    private int ageSurcharge;
    private int delaySurcharge;

    // Calculated partial Discounts
    private int dayDiscount;
    private int repairsDiscount;

    // Calculated partial Prices
    private int basePrice;
    private int totalPrice;
}
