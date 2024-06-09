package com.tickets.tickets.clients;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.tickets.tickets.auxClass.BasePriceRequest;
import com.tickets.tickets.configurations.FeignClientConfig;
import com.tickets.tickets.models.Repair;
import com.tickets.tickets.models.Vehicle;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ms-repairs",
        url = "http://localhost:8080/api/v1/repair",
        configuration = {FeignClientConfig.class})
public interface RepairFeignClient {

    @GetMapping("/byticket/{idTicket}")
    List<Repair> getRepairsByTicket(@PathVariable("idTicket") Long id);

    @PutMapping("/setBasePrice")
    Integer setBasePrice(@RequestBody BasePriceRequest basePriceRequest);

    @GetMapping("/{id}")
    Repair getRepairById(@PathVariable Long id);

    @PutMapping("/calculateKMSurcharge/{percentage}")
    Integer calculateKMSurcharge(@RequestBody Repair repair, @PathVariable double percentage);

    @PutMapping("/calculateAgeSurcharge/{percentage}")
    Integer calculateAgeSurcharge(@RequestBody Repair repair, @PathVariable double percentage);

    @PutMapping("/calculateDelaySurcharge/{percentage}")
    Integer calculateDelaySurcharge(@RequestBody Repair repair, @PathVariable double percentage);

    @PutMapping("/calculateDayDiscount/{percentage}")
    Integer calculateDayDiscount(@RequestBody Repair repair, @PathVariable double percentage);

    @PutMapping("/calculateRepairsDiscount/{percentage}")
    Integer calculateRepairsDiscount(@RequestBody Repair repair, @PathVariable double percentage);

    @PutMapping("/calculateTotalPrice")
    Integer calculateTotalPrice(@RequestBody Repair repair);

}