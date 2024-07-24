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
        System.out.println("ESTOY BUSCANDO POR TICKET XD");
        return repairRepository.findRepairsByIdTicket(idTicket);
    }

    //------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------

    // Here we have the methods to calculate all the values of the repair taking a percentage from the ticket
    // The calculations are the same for all


    // Assuming you have an instance of RepairRepository named repairRepository

    // kmSurcharge;
    public int calculateKMSurcharge(RepairEntity repair, double percentage){
        System.out.println("Calculating km surcharge...");
        int kmSurcharge = (int) (repair.getBasePrice() * percentage);
        System.out.println(kmSurcharge);
        repair.setKmSurcharge(kmSurcharge);
        repairRepository.save(repair);
        return kmSurcharge;
    }

    // ageSurcharge;
    public int calculateAgeSurcharge(RepairEntity repair, double percentage){
        int ageSurcharge = (int) (repair.getBasePrice() * percentage);
        repair.setAgeSurcharge(ageSurcharge);
        repairRepository.save(repair);
        return ageSurcharge;
    }

    // delaySurcharge;
    public int calculateDelaySurcharge(RepairEntity repair, double percentage){
        int delaySurcharge = (int) (repair.getBasePrice() * percentage);
        repair.setDelaySurcharge(delaySurcharge);
        repairRepository.save(repair);
        return delaySurcharge;
    }

    // dayDiscount;
    public int calculateDayDiscount(RepairEntity repair, double percentage){
        int dayDiscount = (int) (repair.getBasePrice() * percentage);
        System.out.println("Day discount: " + dayDiscount);
        repair.setDayDiscount(dayDiscount);
        repairRepository.save(repair);
        return dayDiscount;
    }

    // repairsDiscount;
    public int calculateRepairsDiscount(RepairEntity repair, double percentage){
        int repairsDiscount = (int) (repair.getBasePrice() * percentage);
        repair.setRepairsDiscount(repairsDiscount);
        repairRepository.save(repair);
        return repairsDiscount;
    }

    // totalPrice;
    public int calculateTotalPrice(RepairEntity repair){
        // The total price is the sum of the base price and the surcharges minus the discounts
        int totalPrice = repair.getBasePrice() + repair.getKmSurcharge() + repair.getAgeSurcharge() + repair.getDelaySurcharge() - repair.getDayDiscount() - repair.getRepairsDiscount();
        System.out.println("Total price: " + totalPrice);
        System.out.println("Calculation: " + repair.getBasePrice() + " + " + repair.getKmSurcharge() + " + " + repair.getAgeSurcharge() + " + " + repair.getDelaySurcharge() + " - " + repair.getDayDiscount() + " - " + repair.getRepairsDiscount());
        repair.setTotalPrice(totalPrice);
        repairRepository.save(repair);
        return totalPrice;
    }



    //    basePrice;
    public int calculateBasePrice(RepairEntity repair, int engineType){
        System.out.println("Calculating base price...");
        if (repair == null) {
            System.out.println("Repair is null");
            return -1;
        }
        int repairType = repair.getRepairType();

        System.out.println("Repair type: " + repairType);
        System.out.println("Engine type: " + engineType);

        int[][] repairCosts = {
                {120000, 130000, 350000, 210000, 150000, 100000, 80000, 180000, 150000, 130000, 80000}, // Gasoline
                {120000, 130000, 450000, 210000, 150000, 120000, 80000, 180000, 150000, 140000, 80000}, // Diesel
                {180000, 190000, 700000, 300000, 200000, 450000, 100000, 210000, 180000, 220000, 80000}, // Hybrid
                {220000, 230000, 800000, 300000, 250000, 0, 100000, 250000, 180000, 0, 80000} // Electric
        };

        int basePrice = 0;

        // Check if the repairType and engineType are within the valid range
        if (repairType >= 1 && repairType <= 11 && engineType >= 0 && engineType <= 3) {
            System.out.printf("Repair type: %d\n", repairType);
            System.out.println("Engine type: " + engineType);

            // Calculate the base price from the array
            basePrice = repairCosts[engineType][repairType - 1];
        }

        // Save the base price in the RepairEntity
        System.out.println("Base price: " + basePrice);
        repair.setBasePrice(basePrice);
        repairRepository.save(repair);
        return basePrice;
    }
    // Using a ticket id and a repair type, get all repairs of that type for that ticket and then calculate the total price and the count of repairs of that type
    public List<RepairEntity> getRepairsByIdTicketAndRepairType(Long idTicket, int repairType){
        List<RepairEntity> repairs = repairRepository.findRepairsByIdTicketAndRepairType(idTicket, repairType);
        for (RepairEntity repair : repairs) {
            calculateTotalPrice(repair);
        }
        return repairs;
    }
    // using getRepairsByIdTicketAndRepairType calculate the total price and the count of repairs of that type
    public int getTotalFromAType(Long idTicket, int repairType){
        List<RepairEntity> repairs = repairRepository.findRepairsByIdTicketAndRepairType(idTicket, repairType);
        // i just need to get the total price
        int totalPrice = 0;
        for (RepairEntity repair : repairs) {
            totalPrice += repair.getTotalPrice();
        }

        return totalPrice;
    }
    // idem but with the count
    public int getCountFromAType(Long idTicket, int repairType){
        List<RepairEntity> repairs = repairRepository.findRepairsByIdTicketAndRepairType(idTicket, repairType);
        System.out.println("Repairs: " + repairs.size());
        return repairs.size();
    }





}