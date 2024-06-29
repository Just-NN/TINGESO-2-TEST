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

    @Autowired
    InternalCalculationR1Service internalCalculationR1Service;

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

    // Some calculations
    // get data from ms-tickets
    public List<VehicleRepairEntity> getVehicleRepairs(){
        // get all the tickets using restTemplate
        List<Ticket> tickets = restTemplate.getForObject("http://gateway-server-service/api/v1/ticket/", List.class);
        // get all the vehicle repairs per ticket
        for (Ticket ticket : tickets) {

        }
    }

}
