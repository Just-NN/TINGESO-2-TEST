package com.tickets.tickets.clients;

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

@FeignClient(value = "ms-repairs",
        path = "api/v1/repair",
        configuration = {FeignClientConfig.class}
        )
public interface RepairFeignClient {

    @GetMapping("/byticket/{idTicket}")
    List<Repair> getRepairsByTicket(@PathVariable("idTicket") Long id);

    @PutMapping("/setBasePrice")
    int setBasePrice(@RequestBody Repair repair);

    @GetMapping("/{id}")
    Repair getRepairById(@PathVariable Long id);

    @PutMapping("/calculateKMSurcharge/{percentage}")
    int calculateKMSurcharge(@RequestBody Repair repair, @PathVariable double percentage);

    @PutMapping("/calculateAgeSurcharge/{percentage}")
    public ResponseEntity<Integer> calculateAgeSurcharge(@RequestBody Repair repair, @PathVariable double percentage);

    @PutMapping("/calculateDelaySurcharge/{percentage}")
    int calculateDelaySurcharge(@RequestBody Repair repair, @PathVariable double percentage);

    @PutMapping("/calculateDayDiscount/{percentage}")
    int calculateDayDiscount(@RequestBody Repair repair, @PathVariable double percentage);

    @PutMapping("/calculateRepairsDiscount/{percentage}")
    int calculateRepairsDiscount(@RequestBody Repair repair, @PathVariable double percentage);

    @PutMapping("/calculateTotalPrice")
    int calculateTotalPrice(@RequestBody Repair repair);

}
