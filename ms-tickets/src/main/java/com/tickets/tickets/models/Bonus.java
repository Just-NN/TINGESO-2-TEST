package com.tickets.tickets.models;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bonus {
    private Long idBonus;

    private Long idTicket;

    private String brand;
    private int bonus;
    private Boolean active;
}
