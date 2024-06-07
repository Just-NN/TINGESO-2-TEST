package com.repairs.repairs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import usach.tingeso.entities.RepairEntity;
//import usach.tingeso.entities.TicketEntity;
//import usach.tingeso.entities.VehicleEntity;
//import usach.tingeso.repositories.RepairRepository;
//import usach.tingeso.repositories.TicketRepository;
//import usach.tingeso.repositories.VehicleRepository;
import com.repairs.repairs.entities.RepairEntity;
import com.repairs.repairs.repositories.RepairRepository;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class RepairService {
    @Autowired
    RepairRepository repairRepository;

    // Basic CRUD operations
    public RepairEntity getRepairById(Long id){
        return repairRepository.findById(id).orElse(null);
    }

    public RepairEntity saveRepair(RepairEntity repair) {
        System.out.println("Empecé");
        if (repair == null) {
            System.out.println("HOLA MI REPAIR ES NULA");
            // Handle the case where repair is null
            return null;
        }
        if (repair.getIdTicket() == null || repair.getIdTicket() < 0) {
            System.out.println("HOLA MI TICKET ES NULO");
            // Handle the case where the ticket does not exist
            // Create a ticket with the idTicket
            return null;
        }
        System.out.println("Mi repair es: " + repair.toString());
        System.out.println("LLEGUÉ HASTA EL FINAL");
        return repairRepository.save(repair);
    }

    public void deleteRepair(Long id){
        if (repairRepository.existsById(id)) {
            repairRepository.deleteById(id);
        }
    }

    public RepairEntity updateRepair(RepairEntity repair){
        if (repairRepository.existsById(repair.getIdRepair())) {
            return repairRepository.save(repair);
        }
        return null;
    }

    // Get all repairs
    public List<RepairEntity> getAllRepairs(){
        return repairRepository.findAll();
    }

    // Get all repairs by ticket
    public List<RepairEntity> getRepairsByTicket(Long idTicket){
        return repairRepository.findRepairsByIdTicket(idTicket);
    }

    //------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------

    // Here we have the methods to calculate all the values of the repair taking a percentage from the ticket
    // The calculations are the same for all


    //    kmSurcharge;
    public int calculateKMSurcharge(RepairEntity repair, double percentage){
        return (int) (repair.getKmSurcharge() * percentage);
    }
    //    ageSurcharge;
    public int calculateAgeSurcharge(RepairEntity repair, double percentage){
        return (int) (repair.getAgeSurcharge() * percentage);
    }
    //    delaySurcharge;
    public int calculateDelaySurcharge(RepairEntity repair, double percentage){
        return (int) (repair.getDelaySurcharge() * percentage);
    }
    //    dayDiscount;
    public int calculateDayDiscount(RepairEntity repair, double percentage){
        return (int) (repair.getDayDiscount() * percentage);
    }
    //    repairsDiscount;
    public int calculateRepairsDiscount(RepairEntity repair, double percentage){
        return (int) (repair.getRepairsDiscount() * percentage);
    }
    //    totalPrice;
    public int calculateTotalPrice(RepairEntity repair){
        // The total price is the sum of the base price and the surcharges minus the discounts
        return repair.getBasePrice() + repair.getKmSurcharge() + repair.getAgeSurcharge() + repair.getDelaySurcharge() - repair.getDayDiscount() - repair.getRepairsDiscount();
    }

    //    basePrice;
    public int calculateBasePrice(RepairEntity repair, int engineType){
        if (repair == null) {
            return -1;
        }
        int repairType = repair.getRepairType();

        // Define a 2D array to store the repair costs
        int[][] repairCosts = {
                {120000, 130000, 350000, 210000, 150000, 100000, 80000, 180000, 150000, 130000, 80000}, // Gasoline
                {120000, 130000, 450000, 210000, 150000, 120000, 80000, 180000, 150000, 140000, 80000}, // Diesel
                {180000, 190000, 700000, 300000, 200000, 450000, 100000, 210000, 180000, 220000, 80000}, // Hybrid
                {220000, 230000, 800000, 300000, 250000, 0, 100000, 250000, 180000, 0, 80000} // Electric
        };

        int basePrice = -1;
        // Check if the repairType and engineType are within the valid range
        if (repairType >= 1 && repairType <= 11 && engineType >= 0 && engineType <= 3) {
            System.out.printf("Repair type: %d\n", repairType);
            System.out.println("Engine type: " + engineType);

            // Calculate the base price from the array
            basePrice = repairCosts[engineType][repairType - 1];
        }

        // Save the base price in the RepairEntity
        repair.setBasePrice(basePrice);
        repairRepository.save(repair);
        return basePrice;
    }


}