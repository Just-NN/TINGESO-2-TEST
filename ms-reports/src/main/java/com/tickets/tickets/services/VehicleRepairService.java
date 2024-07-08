package com.tickets.tickets.services;

import com.tickets.tickets.models.Ticket;
import com.tickets.tickets.repositories.VehicleRepairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tickets.tickets.entities.VehicleRepairEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class VehicleRepairService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    VehicleRepairRepository vehicleRepairRepository;



    // Basic CRUD operations
    public VehicleRepairEntity getVehicleRepairById(Long id){
        return vehicleRepairRepository.findById(id).orElse(null);
    }
    public VehicleRepairEntity saveVehicleRepair(VehicleRepairEntity vehicleRepair) {
        if (vehicleRepair == null) {
            // Handle the case where vehicleRepair is null
            return null;
        }
        return vehicleRepairRepository.save(vehicleRepair);
    }
    public void deleteVehicleRepair(Long id){
        if (vehicleRepairRepository.existsById(id)) {
            vehicleRepairRepository.deleteById(id);
        }
    }
    public VehicleRepairEntity updateVehicleRepair(VehicleRepairEntity vehicleRepair){
        if (vehicleRepairRepository.existsById(vehicleRepair.getIdRepair())) {
            return vehicleRepairRepository.save(vehicleRepair);
        }
        return null;
    }
    public void deleteAllVehicleRepairs(){
        vehicleRepairRepository.deleteAll();
    }

    // Reset the values of the vehicle repairs
    public void resetValues(){
        List<VehicleRepairEntity> vehicleRepairs = vehicleRepairRepository.findAllVehicleRepairs();
        for (VehicleRepairEntity vehicleRepair : vehicleRepairs){
            vehicleRepair.setSedanAmount(0);
            vehicleRepair.setHatchbackAmount(0);
            vehicleRepair.setSuvAmount(0);
            vehicleRepair.setPickupAmount(0);
            vehicleRepair.setTruckAmount(0);
            vehicleRepair.setSedanPrice(0);
            vehicleRepair.setHatchbackPrice(0);
            vehicleRepair.setSuvPrice(0);
            vehicleRepair.setPickupPrice(0);
            vehicleRepair.setTruckPrice(0);
            vehicleRepair.setTotalAmount(0);
            vehicleRepair.setTotalPrice(0);
            updateVehicleRepair(vehicleRepair);
        }
    }



}
