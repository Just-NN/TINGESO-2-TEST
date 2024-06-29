package com.tickets.tickets.repositories;

import com.tickets.tickets.entities.R1Entity;
import com.tickets.tickets.entities.VehicleRepairEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface R1Repository extends JpaRepository<R1Entity, Long> {
    //Custom query to get all the vehicle repairs from id 0 to id 11
    // This allows me to get all the data because there are just 12 types of repairs
    @Query("SELECT v FROM VehicleRepairEntity v WHERE v.idRepair BETWEEN 0 AND 11")
    List<VehicleRepairEntity> findVehicleRepairs();
}
