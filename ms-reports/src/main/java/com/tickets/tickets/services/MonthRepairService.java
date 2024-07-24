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
    // Now is necessary to calculate the variations between the third month and the first month and the second month
    public void calculateVariations() {
        List<MonthRepairEntity> monthRepairs = monthRepairRepository.findAllMonthRepairs();
        for (MonthRepairEntity monthRepair : monthRepairs) {
            int firstAmount = monthRepair.getFirstMonthAmount();
            int secondAmount = monthRepair.getSecondMonthAmount();
            int thirdAmount = monthRepair.getThirdMonthAmount();
            int firstPrice = monthRepair.getFirstMonthPrice();
            int secondPrice = monthRepair.getSecondMonthPrice();
            int thirdPrice = monthRepair.getThirdMonthPrice();

            Integer firstAmountVariation = null;
            Integer secondAmountVariation = null;
            Integer firstPriceVariation = null;
            Integer secondPriceVariation = null;

            if (firstAmount != 0) {
                firstAmountVariation = (secondAmount - firstAmount) * 100 / firstAmount;
            }
            if (secondAmount != 0) {
                secondAmountVariation = (thirdAmount - secondAmount) * 100 / secondAmount;
            }
            if (firstPrice != 0) {
                firstPriceVariation = (secondPrice - firstPrice) * 100 / firstPrice;
            }
            if (secondPrice != 0) {
                secondPriceVariation = (thirdPrice - secondPrice) * 100 / secondPrice;
            }

            if (firstAmountVariation != null) {
                monthRepair.setFirstAmountVariation(firstAmountVariation);
            }
            if (secondAmountVariation != null) {
                monthRepair.setSecondAmountVariation(secondAmountVariation);
            }
            if (firstPriceVariation != null) {
                monthRepair.setFirstPriceVariation(firstPriceVariation);
            }
            if (secondPriceVariation != null) {
                monthRepair.setSecondPriceVariation(secondPriceVariation);
            }

            updateMonthRepair(monthRepair);
        }
    }
}

