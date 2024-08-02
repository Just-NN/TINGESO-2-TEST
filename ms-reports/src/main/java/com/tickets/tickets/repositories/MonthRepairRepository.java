package com.tickets.tickets.repositories;

import com.tickets.tickets.entities.MonthRepairEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MonthRepairRepository extends JpaRepository<MonthRepairEntity, Long> {
    // Delete all month repairs
    void deleteAll();

    // Custom query to find all month repairs
    @Query("SELECT m FROM MonthRepairEntity m")
    List<MonthRepairEntity> findAllMonthRepairs();

    // Custom query to find the first month repair entity by repair type
    List<MonthRepairEntity> findMonthRepairEntitiesByRepairType(int repairType);
}