package com.tickets.tickets.clients;

import com.tickets.tickets.configurations.FeignClientConfig;
import com.tickets.tickets.models.Vehicle;
import feign.Feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-vehicles",
        url = "http://localhost:8080/api/v1/vehicle",
        configuration = {FeignClientConfig.class})
public interface VehicleFeignClient {
    @GetMapping("/{licensePlate}")
    Vehicle getVehicleById(@PathVariable("licensePlate") Long licensePlate);
    @GetMapping("/engineType/{id}")
    Integer getEngineType(@PathVariable Long id);

}
