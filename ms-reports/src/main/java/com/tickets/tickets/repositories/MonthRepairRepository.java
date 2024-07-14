package com.tickets.tickets.repositories;

import com.tickets.tickets.entities.MonthRepairEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonthRepairRepository extends JpaRepository<MonthRepairEntity, Long> {
    // Delete all month repairs
    void deleteAll();
    // find all month repairs in db
    List<MonthRepairEntity> findAllMonthRepairs();
    // find month repair by repair type
    MonthRepairEntity findMonthRepairEntityByRepairType(int repairType);
}
