package com.tickets.tickets.repositories;

import com.tickets.tickets.entities.MonthRepairEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MonthRepairRepository extends JpaRepository<MonthRepairEntity, Long> {
    // Delete all month repairs
    void deleteAll();

    // Custom query to find all month repairs (example)
    @Query("SELECT m FROM MonthRepairEntity m")
    List<MonthRepairEntity> findAllMonthRepairs();

    // Find month repair by repair type
    MonthRepairEntity findMonthRepairEntityByRepairType(int repairType);
}