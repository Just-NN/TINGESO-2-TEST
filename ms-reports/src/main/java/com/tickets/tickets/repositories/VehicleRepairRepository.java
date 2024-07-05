package com.tickets.tickets.repositories;

import com.tickets.tickets.entities.VehicleRepairEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehicleRepairRepository extends JpaRepository<VehicleRepairEntity, Long> {
    VehicleRepairEntity findVehicleRepairEntityByRepairType(int repairType);
    // Delete all vehicle repairs
    void deleteAll();
    // find all vehicle repairs in db
    @Query("SELECT v FROM VehicleRepairEntity v")
    List<VehicleRepairEntity> findAllVehicleRepairs();

}
