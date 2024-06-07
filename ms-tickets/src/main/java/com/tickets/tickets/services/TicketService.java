package com.tickets.tickets.services;

import com.tickets.tickets.clients.RepairFeignClient;
import com.tickets.tickets.clients.VehicleFeignClient;
import com.tickets.tickets.entities.TicketEntity;
import com.tickets.tickets.models.Repair;
import com.tickets.tickets.models.Vehicle;
import com.tickets.tickets.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    private InternalCalculationService internalCalculationService;

    RepairFeignClient repairFeignClient;
    VehicleFeignClient vehicleFeignClient;


    // Basic CRUD operations
    public TicketEntity getTicketById(Long id){
        if (id == null) {
            // Handle the case where id is null
            return null;
        }
        return ticketRepository.findById(id).orElse(null);
    }

    public TicketEntity saveTicket(TicketEntity ticket){
        if (ticket == null)
            return null;
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id){
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
        }
    }

    public TicketEntity updateTicket(TicketEntity ticket){
        if (ticketRepository.existsById(ticket.getIdTicket())) {
            return ticketRepository.save(ticket);
        }
        return null;
    }

    // Get all tickets
    public List<TicketEntity> getAllTickets() {
        List<TicketEntity> tickets = ticketRepository.findAll();
        if (tickets == null) {
            return new ArrayList<>(); // return an empty list instead of null
        }
        return tickets;
    }

    //------------------------------------------------------------------------------------------------------------

    //Operations
    // Save base price
    public TicketEntity saveBasePrice(TicketEntity ticket){
        if (ticket == null) {
            // Handle the case where ticket is null
            return null;
        }

        Long ticketId = ticket.getIdTicket();
        System.out.println("Ticket ID: " + ticketId);

        // Use ParameterizedTypeReference for type safety and RestTemplate.exchange for the GET request
        List<Repair> repairs = repairFeignClient.getRepairsByTicket(ticketId);
        System.out.println("Repairs: " + repairs.size());

        double totalBasePrice = 0;

        for (Repair repair : repairs) {
            System.out.println("Repair: " + repair.toString());
            repairFeignClient.setBasePrice(repair);
            // Fetch the updated Repair object
            if (repair == null) {
                // Handle the case where repair is null
                System.out.println("Repair is null");
                return null;
            }
            // restTemplate.getForObject("http://localhost:8093/api/v1/repair/" + repair.getIdRepair(), Repair.class);
            Repair updatedRepair = repairFeignClient.getRepairById(repair.getIdRepair());
            System.out.println("Updated repair: " + updatedRepair.toString());
            totalBasePrice += updatedRepair.getBasePrice();
        }

        ticket.setBasePrice((int) Math.round(totalBasePrice));
        return ticketRepository.save(ticket);
    }

    public TicketEntity saveKMSurcharge(TicketEntity ticket){
        if (ticket == null) {
            // Handle the case where ticket is null
            return null;
        }

        Long ticketId = ticket.getIdTicket();
        System.out.println("Ticket ID: " + ticketId);

        Double percentageKMSurcharge = internalCalculationService.calculateSurchargeForKM(ticket);

//        ResponseEntity<List<Repair>> response = restTemplate.exchange(
//                "http://localhost:8093/api/v1/repair/byticket/" + ticketId,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<Repair>>() {}
//        );
        //List<Repair> repairs = response.getBody();
        List<Repair> repairs = repairFeignClient.getRepairsByTicket(ticketId);
        System.out.println("Repairs: " + repairs.size());

        double totalKMSurcharge = 0;

        for (Repair repair : repairs) {
//            restTemplate.put("http://localhost:8093/api/v1/repair/calculateKMSurcharge/" + percentageKMSurcharge, repair);
            repairFeignClient.calculateKMSurcharge(repair, percentageKMSurcharge);
            // Fetch the updated Repair object
//            Repair updatedRepair = restTemplate.getForObject("http://localhost:8093/api/v1/repair/byticket/" + repair.getIdRepair(), Repair.class);
            Repair updatedRepair = repairFeignClient.getRepairById(repair.getIdRepair());
            int kmSurcharge = updatedRepair.getKmSurcharge();
            System.out.println("KM surcharge: " + kmSurcharge);
            totalKMSurcharge += kmSurcharge;
        }

        ticket.setSurchargeForKM((int) Math.round(totalKMSurcharge * ticket.getBasePrice()));
        return ticketRepository.save(ticket);
    }

    public TicketEntity saveAgeSurcharge(TicketEntity ticket){
        if (ticket == null) {
            // Handle the case where ticket is null
            return null;
        }

        Long ticketId = ticket.getIdTicket();
        System.out.println("Ticket ID: " + ticketId);

        // Use ParameterizedTypeReference for type safety and RestTemplate.exchange for the GET request
//        ResponseEntity<List<Repair>> response = restTemplate.exchange(
//                "http://localhost:8093/api/v1/repair/byticket/" + ticketId,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<Repair>>() {}
//        );
//        List<Repair> repairs = response.getBody();
        List<Repair> repairs = repairFeignClient.getRepairsByTicket(ticketId);
        System.out.println("Repairs: " + repairs.size());

        double percentageAgeSurcharge = internalCalculationService.calculateSurchargeByAge(ticket);
        int totalAgeSurcharge = 0;

        for (Repair repair : repairs) {
//            restTemplate.put("http://localhost:8093/api/v1/repair/calculateKMSurcharge/" + percentageAgeSurcharge, repair);
//            // Fetch the updated Repair object
//            Repair updatedRepair = restTemplate.getForObject("http://localhost:8093/api/v1/repair/byticket/" + repair.getIdRepair(), Repair.class);
            repairFeignClient.calculateAgeSurcharge(repair, percentageAgeSurcharge);
            Repair updatedRepair = repairFeignClient.getRepairById(repair.getIdRepair());
            int ageSurcharge = updatedRepair.getAgeSurcharge();
            System.out.println("Age surcharge: " + ageSurcharge);
            totalAgeSurcharge = ageSurcharge;
        }

        ticket.setSurchargeForAge((int) Math.round(totalAgeSurcharge * ticket.getBasePrice()));
        return ticketRepository.save(ticket);
    }

    public TicketEntity saveSurchargeForDelay(TicketEntity ticket){
        if (ticket == null) {
            // Handle the case where ticket is null
            return null;
        }

        Long ticketId = ticket.getIdTicket();
        System.out.println("Ticket ID: " + ticketId);

        // Use ParameterizedTypeReference for type safety and RestTemplate.exchange for the GET request
//        ResponseEntity<List<Repair>> response = restTemplate.exchange(
//                "http://localhost:8093/api/v1/repair/byticket/" + ticketId,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<Repair>>() {}
//        );
//        List<Repair> repairs = response.getBody();
        List<Repair> repairs = repairFeignClient.getRepairsByTicket(ticketId);
        System.out.println("Repairs: " + repairs.size());

        double percentageDelaySurcharge = internalCalculationService.calculateSurchargeForDelay(ticket);
        int totalDelaySurcharge = 0;

        for (Repair repair : repairs) {
//            restTemplate.put("http://localhost:8093/api/v1/repair/calculateKMSurcharge/" + percentageDelaySurcharge, repair);
            repairFeignClient.calculateDelaySurcharge(repair, percentageDelaySurcharge);
            // Fetch the updated Repair object
//            Repair updatedRepair = restTemplate.getForObject("http://localhost:8093/api/v1/repair/byticket/" + repair.getIdRepair(), Repair.class);
            Repair updatedRepair = repairFeignClient.getRepairById(repair.getIdRepair());
            int delaySurcharge = updatedRepair.getDelaySurcharge();
            System.out.println("Delay surcharge: " + delaySurcharge);
            totalDelaySurcharge = delaySurcharge;
        }

        ticket.setSurchargeForDelay((int) Math.round(totalDelaySurcharge * ticket.getBasePrice()));
        return ticketRepository.save(ticket);
    }

    public TicketEntity saveDiscountByRepairs(TicketEntity ticket){
        if (ticket == null) {
            // Handle the case where ticket is null
            return null;
        }

        Long ticketId = ticket.getIdTicket();
        System.out.println("Ticket ID: " + ticketId);

        // Use ParameterizedTypeReference for type safety and RestTemplate.exchange for the GET request
//        ResponseEntity<List<Repair>> response = restTemplate.exchange(
//                "http://localhost:8093/api/v1/repair/byticket/" + ticketId,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<Repair>>() {}
//        );
//        List<Repair> repairs = response.getBody();
        List<Repair> repairs = repairFeignClient.getRepairsByTicket(ticketId);
        System.out.println("Repairs: " + repairs.size());

        double totalDiscountByRepairs = 0;

        for (Repair repair : repairs) {
//            restTemplate.put("http://localhost:8093/api/v1/repair/calculateDiscountByRepairs", repair);
            repairFeignClient.calculateRepairsDiscount(repair, 0.1);
            // Fetch the updated Repair object
//            Repair updatedRepair = restTemplate.getForObject("http://localhost:8093/api/v1/repair/byticket/" + repair.getIdRepair(), Repair.class);
            Repair updatedRepair = repairFeignClient.getRepairById(repair.getIdRepair());
            double discountByRepairs = updatedRepair.getRepairsDiscount();
            System.out.println("Discount by repairs: " + discountByRepairs);
            totalDiscountByRepairs += discountByRepairs;
        }

        ticket.setDiscountForRepairs((int) Math.round(totalDiscountByRepairs * ticket.getBasePrice()));
        return ticketRepository.save(ticket);
    }

    public TicketEntity saveDiscountByDay(TicketEntity ticket){
        if (ticket == null) {
            // Handle the case where ticket is null
            return null;
        }

        Long ticketId = ticket.getIdTicket();
        System.out.println("Ticket ID: " + ticketId);

        // Use ParameterizedTypeReference for type safety and RestTemplate.exchange for the GET request
//        ResponseEntity<List<Repair>> response = restTemplate.exchange(
//                "http://localhost:8093/api/v1/repair/byticket/" + ticketId,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<Repair>>() {}
//        );
//        List<Repair> repairs = response.getBody();
        List<Repair> repairs = repairFeignClient.getRepairsByTicket(ticketId);
        System.out.println("Repairs: " + repairs.size());

        double totalDiscountByDay = 0;

        for (Repair repair : repairs) {
//            restTemplate.put("http://localhost:8093/api/v1/repair/calculateDiscountByDay", repair);
            repairFeignClient.calculateDayDiscount(repair, 0.1);
            // Fetch the updated Repair object
//            Repair updatedRepair = restTemplate.getForObject("http://localhost:8093/api/v1/repair/byticket/" + repair.getIdRepair(), Repair.class);
            Repair updatedRepair = repairFeignClient.getRepairById(repair.getIdRepair());
            double discountByDay = updatedRepair.getDayDiscount();
            System.out.println("Discount by day: " + discountByDay);
            totalDiscountByDay += discountByDay;
        }

        ticket.setDiscountPerDay((int) Math.round(totalDiscountByDay * ticket.getBasePrice()));
        return ticketRepository.save(ticket);
    }



    // Total price
    public TicketEntity saveTotalPrice(TicketEntity ticket){
        if (ticket == null) {
            // Handle the case where ticket is null
            return null;
        }
//        List<Repair> repairs = restTemplate.getForObject("http://localhost:8093/api/v1/repair/byticket/" + ticket.getIdTicket(), List.class);
        List<Repair> repairs = repairFeignClient.getRepairsByTicket(ticket.getIdTicket());
        double totalPrice = 0;
        if (repairs.isEmpty()) {
            System.out.println("No repairs found for ticket ID: " + ticket.getIdTicket());
            return null;
        }
        totalPrice = internalCalculationService.calculateTotalPrice(ticket);
        ticket.setTotalPrice((int) Math.round(totalPrice));
        System.out.println("Total price: " + ticket.getTotalPrice());
        System.out.println("Saved Ticket: " + ticket.toString());
        return ticketRepository.save(ticket);
    }

    // A init method to call all the operations before saving the ticket
    public TicketEntity saveTicketWithOperations(TicketEntity ticket){
        if (ticket == null) {
            // Handle the case where ticket is null
            return null;
        }
        ticket = saveBasePrice(ticket);
        ticket = saveKMSurcharge(ticket);
        ticket = saveAgeSurcharge(ticket);
        ticket = saveSurchargeForDelay(ticket);
        ticket = saveDiscountByRepairs(ticket);
        ticket = saveDiscountByDay(ticket);
        ticket = saveTotalPrice(ticket);
        return ticketRepository.save(ticket);
    }

}