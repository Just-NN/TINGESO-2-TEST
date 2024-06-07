package com.tickets.tickets.clients;

import com.tickets.tickets.configurations.FeignClientConfig;
import com.tickets.tickets.models.Vehicle;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "ms-vehicles",
        path = "api/v1/vehicle",
        configuration = {FeignClientConfig.class}
        )
public interface VehicleFeignClient {
    @GetMapping
    Vehicle getVehicleById(Long id);

}
