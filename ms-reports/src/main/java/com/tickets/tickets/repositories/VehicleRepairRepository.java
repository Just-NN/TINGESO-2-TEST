package com.tickets.tickets.repositories;

import com.tickets.tickets.entities.VehicleRepairEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VehicleRepairRepository extends JpaRepository<VehicleRepairEntity, Long> {
}
