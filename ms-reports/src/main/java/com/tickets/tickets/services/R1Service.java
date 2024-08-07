package com.tickets.tickets.services;

import com.tickets.tickets.entities.MonthRepairEntity;
import com.tickets.tickets.entities.R1Entity;
import com.tickets.tickets.entities.VehicleRepairEntity;
import com.tickets.tickets.repositories.R1Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class R1Service {

    @Autowired
    R1Repository r1Repository;
    @Autowired
    InternalCalculationR1Service internalCalculationR1Service;
    @Autowired
    InternalCalculationR2Service internalCalculationR2Service;
    @Autowired
    VehicleRepairService vehicleRepairService;
    @Autowired
    MonthRepairService monthRepairService;

    // Crud
    public R1Entity getR1ById(Long id){
        return r1Repository.findById(id).orElse(null);
    }
    public R1Entity saveR1(R1Entity r1) {
        if (r1 == null) {
            // Handle the case where r1 is null
            return null;
        }
        return r1Repository.save(r1);
    }
    public void deleteR1(Long id){
        if (r1Repository.existsById(id)) {
            r1Repository.deleteById(id);
        }
    }
    public R1Entity updateR1(R1Entity r1){
        if (r1Repository.existsById(r1.getIdR1())) {
            return r1Repository.save(r1);
        }
        return null;
    }

    public void deleteAllR1(){
        r1Repository.deleteAll();
    }

    // Initialize the values
    public void initializeValuesR1(R1Entity r1Entity, int month, int year){
        System.out.println("Initializing values");
        System.out.println("Calculating R1");
        List<Long> values = internalCalculationR1Service.calculateR1();
        List<Long> values2 = internalCalculationR2Service.calculateR2(month, year);
        monthRepairService.calculateVariations();
        System.out.println("Calculating total values");
        internalCalculationR1Service.calculateTotalValues();
        System.out.println("Values: " + values.toString());
        if (values == null){
            System.out.println("Values are null in initializeValues");
            return;
        }
        if (values2 == null){
            System.out.println("Values2 are null in initializeValues");
            return;
        }
        r1Entity.setVehicleRepairIds(values);
        r1Entity.setMonthRepairIds(values2);
        saveR1(r1Entity);
        System.out.println("Values initialized");
        saveR1(r1Entity);
    }


    // create empty R1Entity and save it
    public void createEmptyR1(){
        R1Entity r1Entity = new R1Entity();
        r1Entity.setIdR1(1L);
        //empty list in repair ids
        r1Entity.setVehicleRepairIdsEmpty(List.of());
        System.out.println("Creating empty R1Entity");
        System.out.println(r1Entity.toString());
        saveR1(r1Entity);
    }
    // get all the repairs in the r1 - the content using each id from 0 to 11
    public List<VehicleRepairEntity> getVehicleRepairIds(Long id){
        R1Entity r1Entity = getR1ById(id);
        List<VehicleRepairEntity> vehicleRepairs = new ArrayList<>();
        if (r1Entity == null){
            return null;
        }
        for (int i = 0; i < 12; i++){
            VehicleRepairEntity vehicleRepair = vehicleRepairService.getVehicleRepairById(r1Entity.getVehicleRepairIds().get(i));
            if (vehicleRepair != null){
                vehicleRepairs.add(vehicleRepair);
            }
        }
        return vehicleRepairs;
    }
    // get all the month repairs in the r1 - the content using each id from 0 to 11
    public List<MonthRepairEntity> getMonthRepairIds(Long id){
        R1Entity r1Entity = getR1ById(id);
        List<MonthRepairEntity> monthRepairs = new ArrayList<>();
        if (r1Entity == null){
            return null;
        }
        for (int i = 0; i < 12; i++){
            MonthRepairEntity monthRepairEntity = monthRepairService.getMonthRepairById(r1Entity.getMonthRepairIds().get(i));
            if (monthRepairEntity != null){
                monthRepairs.add(monthRepairEntity);
            }
        }
        return monthRepairs;
    }

}
