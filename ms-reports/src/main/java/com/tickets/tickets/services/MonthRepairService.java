package com.tickets.tickets.services;

import com.tickets.tickets.entities.MonthRepairEntity;
import com.tickets.tickets.models.Ticket;
import com.tickets.tickets.repositories.MonthRepairRepository;
import com.tickets.tickets.repositories.VehicleRepairRepository;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tickets.tickets.entities.VehicleRepairEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MonthRepairService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MonthRepairRepository monthRepairRepository;



    // Basic CRUD operations
    public MonthRepairEntity getMonthRepairById(Long id){
        return monthRepairRepository.findById(id).orElse(null);
    }
    public MonthRepairEntity saveMonthRepair(MonthRepairEntity monthRepair) {
        if (monthRepair == null) {
            // Handle the case where monthRepair is null
            return null;
        }
        return monthRepairRepository.save(monthRepair);
    }
    public void deleteMonthRepair(Long id){
        if (monthRepairRepository.existsById(id)) {
            monthRepairRepository.deleteById(id);
        }
    }
    public MonthRepairEntity updateMonthRepair(MonthRepairEntity monthRepair) {
        if (monthRepairRepository.existsById(monthRepair.getIdRepair())) {
            return monthRepairRepository.save(monthRepair);
        }
        return null;
    }
    public void deleteAllMonthRepairs(){
        monthRepairRepository.deleteAll();
    }


    // Reset the values of the month repairs
    public void resetValues(){
        List<MonthRepairEntity> monthRepairs = monthRepairRepository.findAllMonthRepairs();
        for (MonthRepairEntity monthRepair : monthRepairs){
            monthRepair.setFirstMonth(0);
            monthRepair.setSecondMonth(0);
            monthRepair.setThirdMonth(0);
            monthRepair.setFirstMonthAmount(0);
            monthRepair.setFirstMonthPrice(0);
            monthRepair.setSecondMonthAmount(0);
            monthRepair.setSecondMonthPrice(0);
            monthRepair.setThirdMonthAmount(0);
            monthRepair.setThirdMonthPrice(0);
            monthRepair.setFirstAmountVariation(0);
            monthRepair.setFirstPriceVariation(0);
            monthRepair.setSecondAmountVariation(0);
            monthRepair.setSecondPriceVariation(0);
            updateMonthRepair(monthRepair);
        }
    }



}
