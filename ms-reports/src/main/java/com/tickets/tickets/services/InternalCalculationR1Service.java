package com.tickets.tickets.services;

import com.tickets.tickets.entities.R1Entity;
import com.tickets.tickets.entities.VehicleRepairEntity;
import com.tickets.tickets.repositories.VehicleRepairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class InternalCalculationR1Service {


    @Autowired
    VehicleRepairRepository vehicleRepairRepository;
    @Autowired
    RestTemplate restTemplate;

    // Method to count each type of repair taking a list of integers as parameter



    public List<Long> calculateR1(){
        String url = "http://gateway-server-service:8080/api/v1/repair/totalFromAllTypes";
        ResponseEntity<List<List<Integer>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<List<Integer>>>() {}
        );
        List<List<Integer>> repairs = response.getBody();
        // It must give a list of 4x12 lists with 4 elements each one
        // The first element is the vehicle type
        // The second element is the repair type
        // The third element is the amount of repairs
        // The fourth element is the price of the repair
        // Sedan is 0, Hatchback is 1, SUV is 2, Pickup is 3 and Truck is 4
        // Now is important to get each value and set them to VehicleRepairEntity
        // I need the conditionals to set the values
        if (repairs == null){
            return null;
        }
        for (List<Integer> repair : repairs){
            // Must look what repair type is and then what vehicle type is
            VehicleRepairEntity vehicleRepairEntity = vehicleRepairRepository.findVehicleRepairEntityByRepairType(repair.get(1));
            if (vehicleRepairEntity == null){
                vehicleRepairEntity = new VehicleRepairEntity();
                vehicleRepairEntity.setRepairType(repair.get(1));
            }
            int vehicleType = repair.get(0);
            int amount = repair.get(2);
            int price = repair.get(3);
            setVehicleTypeValues(vehicleRepairEntity, vehicleType, amount, price);

        }
        List<VehicleRepairEntity> vehicleRepairs = vehicleRepairRepository.findAllVehicleRepairs();
        if (vehicleRepairs == null){
            vehicleRepairs = createVehicleRepairs();
        }
        // return long list from 0 to 11
        List<Long> repairIds = new ArrayList<>();
        for (VehicleRepairEntity vehicleRepairEntity : vehicleRepairs){
            vehicleRepairRepository.save(vehicleRepairEntity);
            repairIds.add(vehicleRepairEntity.getIdRepair());
        }
        return repairIds;

    }
    private void setVehicleTypeValues(VehicleRepairEntity vehicleRepairEntity, int vehicleType, int amount, int price) {
        int amountPrev;
        int pricePrev;
        switch (vehicleType) { // Assuming the first element is the vehicle type
            case 0: // Sedan
                amountPrev = vehicleRepairEntity.getSedanAmount();
                pricePrev = vehicleRepairEntity.getSedanPrice();
                vehicleRepairEntity.setSedanAmount(amountPrev + amount);
                vehicleRepairEntity.setSedanPrice(pricePrev + price);
            case 1: // Hatchback
                amountPrev = vehicleRepairEntity.getHatchbackAmount();
                pricePrev = vehicleRepairEntity.getHatchbackPrice();
                vehicleRepairEntity.setHatchbackAmount(amountPrev + amount);
                vehicleRepairEntity.setHatchbackPrice(pricePrev + price);
            case 2: // SUV
                amountPrev = vehicleRepairEntity.getSuvAmount();
                pricePrev = vehicleRepairEntity.getSuvPrice();
                vehicleRepairEntity.setSuvAmount(amountPrev + amount);
                vehicleRepairEntity.setSuvPrice(pricePrev + price);
            case 3: // Pickup
                amountPrev = vehicleRepairEntity.getPickupAmount();
                pricePrev = vehicleRepairEntity.getPickupPrice();
                vehicleRepairEntity.setPickupAmount(amountPrev + amount);
                vehicleRepairEntity.setPickupPrice(pricePrev + price);
            case 4: // Truck
                amountPrev = vehicleRepairEntity.getTruckAmount();
                pricePrev = vehicleRepairEntity.getTruckPrice();
                vehicleRepairEntity.setTruckAmount(amountPrev + amount);
                vehicleRepairEntity.setTruckPrice(pricePrev + price);
        }
    }
    // create 12 vehicle repairs with default values (all = 0 except for repairtype which is 0 to 11)
    public List<VehicleRepairEntity> createVehicleRepairs(){
        List<VehicleRepairEntity> vehicleRepairEntities = new ArrayList<>();
        for (int i = 0; i < 12; i++){
            VehicleRepairEntity vehicleRepairEntity = new VehicleRepairEntity();
            vehicleRepairEntity.setIdRepair((long) i);
            vehicleRepairEntity.setRepairType(i);
            vehicleRepairEntities.add(vehicleRepairEntity);
        }
        return vehicleRepairEntities;
    }





}

// MUST CREATE THE MICROSERVICES AND CONNECT THEM TO THIS ONE TO GET THE DATA AND WORK WITH IT
