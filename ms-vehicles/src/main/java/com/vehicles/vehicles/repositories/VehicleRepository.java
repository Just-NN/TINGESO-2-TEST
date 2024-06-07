package com.vehicles.vehicles.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.vehicles.vehicles.entities.VehicleEntity;
@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long>{
    VehicleEntity findVehicleByLicensePlate(Long id);



}
