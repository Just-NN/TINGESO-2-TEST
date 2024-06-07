package com.vehicles.vehicles.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vehicles.vehicles.entities.VehicleEntity;
import com.vehicles.vehicles.services.VehicleService;

import java.util.List;

@RestController
@RequestMapping("api/v1/vehicle")
@CrossOrigin(origins = "*")
public class VehicleController {

    @Autowired
    VehicleService vehicleService;

    @GetMapping("/{id}")
    public ResponseEntity<VehicleEntity> getVehicleById(@PathVariable Long id){
        VehicleEntity vehicle = vehicleService.getVehicleById(id);
        if (vehicle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(vehicle);
    }

    @PostMapping("/")
    public ResponseEntity<VehicleEntity> saveVehicle(@RequestBody VehicleEntity vehicle){
        if (vehicle == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(vehicleService.saveVehicle(vehicle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id){
        VehicleEntity vehicle = vehicleService.getVehicleById(id);
        if (vehicle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/")
    public ResponseEntity<VehicleEntity> updateVehicle(@RequestBody VehicleEntity vehicle){
        if (vehicle == null || vehicleService.getVehicleById(vehicle.getLicensePlate()) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(vehicleService.updateVehicle(vehicle));
    }

    @GetMapping("/")
    public ResponseEntity<List<VehicleEntity>> getAllVehicles(){
        System.out.println("Get all vehicles");
        List<VehicleEntity> vehicles = vehicleService.getAllVehicles();
        if (vehicles == null || vehicles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(vehicles);
    }
}