package com.tickets.tickets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tickets.tickets.entities.TicketEntity;

import java.util.Date;
import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    TicketEntity findTicketByIdTicket(Long id);

    @Query("SELECT t.surchargeForKM, t.surchargeForAge, t.surchargeForDelay, t.discountForRepairs, t.discountPerDay, t.discountForBonus, t.brandBonus, t.basePrice, t.totalPrice FROM TicketEntity t WHERE t.idTicket = :id")
    Object[] getTicketValues(@Param("id") Long id);
    // A custom query to get all the tickets associated to a vehicle this last 12 months
    @Query("SELECT t FROM TicketEntity t WHERE t.licensePlate = :licensePlate AND t.pickupDate >= :date")
    List<TicketEntity> findTicketsByVehicleThisYear(@Param("licensePlate") Long licensePlate, @Param("date") Date date);

    // Custom query to get all the tickets filtered by vehicletype
    @Query("SELECT t FROM TicketEntity t WHERE t.vehicleType = :vehicleType")
    List<TicketEntity> findTicketsByVehicleType(@Param("vehicleType") int vehicleType);


}
