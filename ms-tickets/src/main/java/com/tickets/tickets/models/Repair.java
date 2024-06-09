package com.tickets.tickets.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Repair {
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