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
    @Autowired
    private VehicleRepairService vehicleRepairService;

    // Method to count each type of repair taking a list of integers as parameter



    public List<Long> calculateR1(){
        String url = "http://gateway-server-service:8080/api/v1/ticket/repairs/totalFromAllTypes";
        ResponseEntity<List<List<Integer>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<List<Integer>>>() {}
        );
        List<List<Integer>> repairs = response.getBody();
        // First, I must set all the vehicle repairs in the database to 0
        vehicleRepairService.resetValues();
        System.out.println("All values reset");
        System.out.println("Repairs: " + repairs.toString());
        // It must give a list of 4x12 lists with 4 elements each one
        // The first element is the vehicle type
        // The second element is the repair type
        // The third element is the amount of repairs
        // The fourth element is the price of the repair
        // Sedan is 0, Hatchback is 1, SUV is 2, Pickup is 3 and Truck is 4
        // Now is important to get each value and set them to VehicleRepairEntity
        // I need the conditionals to set the values
        if (repairs == null){
            System.out.println("Repairs are null");
            return null;
        }
        int totalAmount = 0;
        int totalPrice = 0;
        for (List<Integer> repair : repairs){
            System.out.println("Repair: " + repair.toString());
            // Must look what repair type is and then what vehicle type is
            VehicleRepairEntity vehicleRepairEntity = vehicleRepairRepository.findVehicleRepairEntityByRepairType(repair.get(1));
            if (vehicleRepairEntity == null){
                System.out.println("Vehicle repair entity is null");
                vehicleRepairEntity = new VehicleRepairEntity();
                vehicleRepairEntity.setRepairType(repair.get(1));
            }
            int vehicleType = repair.get(0);
            int amount = repair.get(2);
            int price = repair.get(3);
            totalAmount += amount;
            totalPrice += price;
            System.out.println("* Vehicle type: " + vehicleType);
            System.out.println("* Amount: " + amount);
            System.out.println("* Price: " + price);
            setVehicleTypeValues(vehicleRepairEntity, vehicleType, amount, price);
            vehicleRepairRepository.save(vehicleRepairEntity);
            System.out.println("Vehicle repair entity saved: " + vehicleRepairEntity.toString());

        }
        List<VehicleRepairEntity> vehicleRepairs = vehicleRepairRepository.findAllVehicleRepairs();
        if (vehicleRepairs == null){
            vehicleRepairs = createVehicleRepairs();
        }
        // return long list from 0 to 11
        List<Long> repairIds = new ArrayList<>();
        vehicleRepairRepository.saveAll(vehicleRepairs);
        System.out.println("Vehicle repairs: " + vehicleRepairs.toString());
        // filling the list with the ids from 0 to 11
        for (int i = 0; i < 12; i++){
            repairIds.add((long) i);
        }
        System.out.println("Repair ids: " + repairIds.toString());
        return repairIds;
    }
    private void setVehicleTypeValues(VehicleRepairEntity vehicleRepairEntity, int vehicleType, int amount, int price) {
        int amountPrev;
        int pricePrev;
        switch (vehicleType-1) { // Assuming the first element is the vehicle type
            case 0: // Sedan
                System.out.println("Es un sedan");
                amountPrev = vehicleRepairEntity.getSedanAmount();
                pricePrev = vehicleRepairEntity.getSedanPrice();
                vehicleRepairEntity.setSedanAmount(amountPrev + amount);
                vehicleRepairEntity.setSedanPrice(pricePrev + price);
                break;
            case 1: // Hatchback
                System.out.println("Es un hatchback");
                amountPrev = vehicleRepairEntity.getHatchbackAmount();
                pricePrev = vehicleRepairEntity.getHatchbackPrice();
                vehicleRepairEntity.setHatchbackAmount(amountPrev + amount);
                vehicleRepairEntity.setHatchbackPrice(pricePrev + price);
                break;
            case 2: // SUV
                System.out.println("Es un suv");
                amountPrev = vehicleRepairEntity.getSuvAmount();
                pricePrev = vehicleRepairEntity.getSuvPrice();
                vehicleRepairEntity.setSuvAmount(amountPrev + amount);
                vehicleRepairEntity.setSuvPrice(pricePrev + price);
                break;
            case 3: // Pickup
                System.out.println("Es un pickup");
                amountPrev = vehicleRepairEntity.getPickupAmount();
                pricePrev = vehicleRepairEntity.getPickupPrice();
                vehicleRepairEntity.setPickupAmount(amountPrev + amount);
                vehicleRepairEntity.setPickupPrice(pricePrev + price);
                break;
            case 4: // Truck
                System.out.println("Es un truck");
                amountPrev = vehicleRepairEntity.getTruckAmount();
                pricePrev = vehicleRepairEntity.getTruckPrice();
                vehicleRepairEntity.setTruckAmount(amountPrev + amount);
                vehicleRepairEntity.setTruckPrice(pricePrev + price);
                break;
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
