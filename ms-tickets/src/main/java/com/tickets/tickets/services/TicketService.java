package com.tickets.tickets.services;

import com.tickets.tickets.auxClass.BasePriceRequest;
import com.tickets.tickets.entities.TicketEntity;
import com.tickets.tickets.models.Repair;
import com.tickets.tickets.models.Vehicle;
import com.tickets.tickets.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    private InternalCalculationService internalCalculationService;


    @Autowired
    RestTemplate restTemplate;


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

    // Save the type of repair for each repair linked to my ticket
    public TicketEntity saveTypeOfRepairs(TicketEntity ticket){
        if (ticket == null) {
            // Handle the case where ticket is null
            return null;
        }

        Long ticketId = ticket.getIdTicket();
        System.out.println("Ticket ID: " + ticketId);

        // Use ParameterizedTypeReference for type safety and RestTemplate.exchange for the GET request
        ResponseEntity<List<Repair>> response = restTemplate.exchange(
                "http://gateway-server-service:8080/api/v1/repair/byticket/" + ticketId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Repair>>() {}
        );
        List<Repair> repairs = response.getBody();
        System.out.println("Repairs: " + repairs.size());

        List<Integer> typeOfRepairs = new ArrayList<>();
        for (Repair repair : repairs) {
            // add the type of repairs to my list
            typeOfRepairs.add(repair.getRepairType());
        }
        ticket.setTypeOfRepairs(typeOfRepairs);
        return ticketRepository.save(ticket);
    }
    // get the type of repairs for a ticket
    public List<Integer> getTypeOfRepairs(Long ticketId){
        TicketEntity ticket = ticketRepository.findById(ticketId).orElse(null);
        if (ticket == null) {
            return null;
        }
        return ticket.getTypeOfRepairs();
    }


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
        System.out.println("ESTOY HACIENDO LA REQUEST");
        ResponseEntity<List<Repair>> response = restTemplate.exchange(
                "http://gateway-server-service:8080/api/v1/repair/byticket/" + ticketId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Repair>>() {}
        );
        List<Repair> repairs = response.getBody();
        System.out.println("ESTA ES LA LICENCIA: "+ ticket.getLicensePlate());
        int engineType = restTemplate.getForObject("http://gateway-server-service:8080/api/v1/vehicle/engineType/" + ticket.getLicensePlate(), Integer.class);
        System.out.println("Engine type: " + engineType);
        System.out.println("Repairs: " + repairs.size());

        double totalBasePrice = 0;

        for (Repair repair : repairs) {
            System.out.println("--------------------");
            System.out.println("Engine type: " + engineType);
            System.out.println("Repair: " + repair.toString());
            BasePriceRequest basePriceRequest = new BasePriceRequest();
            basePriceRequest.setRepair(repair);
            basePriceRequest.setEngineType(engineType);
            System.out.println("Base price request: " + basePriceRequest.getEngineType());
            System.out.println("Base price request: " + basePriceRequest.getRepair());
            restTemplate.put("http://gateway-server-service:8080/api/v1/repair/setBasePrice", basePriceRequest);
            // Fetch the updated Repair object
            ResponseEntity<Repair> updatedRepairResponse = restTemplate.exchange(
                    "http://gateway-server-service:8080/api/v1/repair/" + repair.getIdRepair(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Repair>() {}
            );
            Repair updatedRepair = updatedRepairResponse.getBody();
            System.out.println("Updated repair: " + updatedRepair.toString());
            totalBasePrice += updatedRepair.getBasePrice();
        }

        ticket.setBasePrice((int) Math.round(totalBasePrice));
        return ticketRepository.save(ticket);
    }


    public TicketEntity saveKMSurcharge(TicketEntity ticket){
        if (ticket == null) {
            return null;
        }

        Long ticketId = ticket.getIdTicket();
        double percentageKMSurcharge = internalCalculationService.calculateSurchargeForKM(ticket);

        ResponseEntity<List<Repair>> response = restTemplate.exchange(
                "http://gateway-server-service:8080/api/v1/repair/byticket/" + ticketId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Repair>>() {}
        );
        List<Repair> repairs = response.getBody();

        double totalKMSurcharge = 0;

        for (Repair repair : repairs) {
            restTemplate.put("http://gateway-server-service:8080/api/v1/repair/calculateKMSurcharge/" + percentageKMSurcharge, repair);
            Repair updatedRepair = restTemplate.getForObject("http://gateway-server-service:8080/api/v1/repair/" + repair.getIdRepair(), Repair.class);
            int kmSurcharge = updatedRepair.getKmSurcharge();
            totalKMSurcharge += kmSurcharge;
        }

        System.out.println("KMSurcharge ANTES DE GUARDAR: " + ticket.getSurchargeForKM());
        return ticketRepository.save(ticket);
    }

    public TicketEntity saveAgeSurcharge(TicketEntity ticket){
        if (ticket == null) {
            return null;
        }

        Long ticketId = ticket.getIdTicket();
        double percentageAgeSurcharge = internalCalculationService.calculateSurchargeByAge(ticket);

        ResponseEntity<List<Repair>> response = restTemplate.exchange(
                "http://gateway-server-service:8080/api/v1/repair/byticket/" + ticketId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Repair>>() {}
        );
        List<Repair> repairs = response.getBody();

        int totalAgeSurcharge = 0;

        for (Repair repair : repairs) {
            restTemplate.put("http://gateway-server-service:8080/api/v1/repair/calculateAgeSurcharge/" + percentageAgeSurcharge, repair);
            Repair updatedRepair = restTemplate.getForObject("http://gateway-server-service:8080/api/v1/repair/" + repair.getIdRepair(), Repair.class);
            int ageSurcharge = updatedRepair.getAgeSurcharge();
            totalAgeSurcharge += ageSurcharge;
        }

        ticket.setSurchargeForAge(percentageAgeSurcharge);
        return ticketRepository.save(ticket);
    }

    public TicketEntity saveSurchargeForDelay(TicketEntity ticket){
        if (ticket == null) {
            // Handle the case where ticket is null
            return null;
        }

        Long ticketId = ticket.getIdTicket();
        System.out.println("Ticket ID xx: " + ticketId);

        // Use ParameterizedTypeReference for type safety and RestTemplate.exchange for the GET request
        ResponseEntity<List<Repair>> response = restTemplate.exchange(
                "http://gateway-server-service:8080/api/v1/repair/byticket/" + ticketId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Repair>>() {}
        );
        List<Repair> repairs = response.getBody();

        System.out.println("Repairs: " + repairs.size());

        double percentageDelaySurcharge = internalCalculationService.calculateSurchargeForDelay(ticket);
        System.out.println("HASTA ACÁ FUNCIONA BIEN");
        int totalDelaySurcharge = 0;

        for (Repair repair : repairs) {
            System.out.println("REPAIR: " + repair.toString());
            System.out.println("ENTRÓ AL FOR");
            restTemplate.put("http://gateway-server-service:8080/api/v1/repair/calculateDelaySurcharge/" + percentageDelaySurcharge, repair);
            // Fetch the updated Repair object
            System.out.println("PASÓ EL PUT");
            ResponseEntity<Repair> updatedRepairResponse = restTemplate.exchange(
                                "http://gateway-server-service:8080/api/v1/repair/" + repair.getIdRepair(),
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<Repair>() {}
                        );
            Repair updatedRepair = updatedRepairResponse.getBody();
            System.out.println("PASÓ LA SOLICITUD");
            int delaySurcharge = updatedRepair.getDelaySurcharge();
            System.out.println("Delay surcharge: " + delaySurcharge);
            totalDelaySurcharge = delaySurcharge;
        }

        System.out.println("PASÓ EL FOR");
        ticket.setSurchargeForDelay(percentageDelaySurcharge);
        return ticketRepository.save(ticket);
    }


    public TicketEntity saveDiscountByRepairs(TicketEntity ticket){
        if (ticket == null) {
            return null;
        }

        Long ticketId = ticket.getIdTicket();
        ResponseEntity<List<Repair>> response = restTemplate.exchange(
                "http://gateway-server-service:8080/api/v1/repair/byticket/" + ticketId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Repair>>() {}
        );
        List<Repair> repairs = response.getBody();

        double totalDiscountByRepairs = 0;

        double discountByRepair = internalCalculationService.calculateDiscountByRepairs(ticket);
        ticket.setDiscountForRepairs((int) Math.round(discountByRepair * ticket.getBasePrice()));
        return ticketRepository.save(ticket);

    }

    public TicketEntity saveDiscountByDay(TicketEntity ticket){
        if (ticket == null) {
            return null;
        }

        Long ticketId = ticket.getIdTicket();
        ResponseEntity<List<Repair>> response = restTemplate.exchange(
                "http://gateway-server-service:8080/api/v1/repair/byticket/" + ticketId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Repair>>() {}
        );
        List<Repair> repairs = response.getBody();

        double totalDiscountByDay = 0;
        double discountByDay = internalCalculationService.calculateDiscountByDay(ticket);

        for (Repair repair : repairs) {
            System.out.println("Repair: " + repair.toString());
            restTemplate.put("http://gateway-server-service:8080/api/v1/repair/calculateDiscountByDay", repair);
            Repair updatedRepair = restTemplate.getForObject("http://gateway-server-service:8080/api/v1/repair/" + repair.getIdRepair(), Repair.class);
            if (updatedRepair != null) {
                totalDiscountByDay += updatedRepair.getDayDiscount();
            }
        }
        System.out.println("Total discount by day: " + totalDiscountByDay);
        ticket.setDiscountPerDay(discountByDay);
        return ticketRepository.save(ticket);
    }
    public TicketEntity saveBonusBrand(TicketEntity ticket) {
        if (ticket == null) {
            System.out.println("Ticket is null");
            return null;
        }
        Long licensePlate = ticket.getLicensePlate();
        try {
            Vehicle vehicle = restTemplate.getForObject("http://gateway-server-service:8080/api/v1/vehicle/" + licensePlate, Vehicle.class);
            if (vehicle == null) {
                System.out.println("Vehicle not found for license plate: " + licensePlate);
                return null;
            }
            String brand = vehicle.getBrand();
            HttpHeaders headers = new HttpHeaders();
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://gateway-server-service:8080/api/v1/bonusBrand/highestActive/" + brand + "/" + ticket.getIdTicket());
            ResponseEntity<Integer> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), Integer.class);
            Integer bonus = response.getBody();
            if (bonus == null || bonus == 0) {
                System.out.println("No bonus found for brand: " + brand);
                return null;
            }
            ticket.setBrandBonus(bonus);
            return ticketRepository.save(ticket);
        } catch (Exception e) {
            System.out.println("Error fetching bonus brand for ticket: " + ticket.getIdTicket() + " - " + e.getMessage());
            ticket.setBrandBonus(0);
            return ticketRepository.save(ticket);
        }
    }

    public TicketEntity saveTotalPrice(TicketEntity ticket){
        if (ticket == null) {
            return null;
        }
        ResponseEntity<List<Repair>> response = restTemplate.exchange(
                "http://gateway-server-service:8080/api/v1/repair/byticket/" + ticket.getIdTicket(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Repair>>() {}
        );
        List<Repair> repairs = response.getBody();
        double totalPrice = 0;
        if (repairs.isEmpty()) {
            return null;
        }
        totalPrice = internalCalculationService.calculateTotalPrice(ticket);
        ticket.setTotalPrice((int) Math.round(totalPrice));
        return ticketRepository.save(ticket);
    }

    public TicketEntity saveTicketWithOperations(TicketEntity ticket){
        if (ticket == null) {
            System.out.println("Ticket is null");
            return null;
        }
        ticket = saveTypeOfRepairs(ticket);
        ticket = saveBasePrice(ticket);
        ticket = saveKMSurcharge(ticket);
        System.out.println("testeando saving todo");
        System.out.println("GUARDO EL KM XD: "+ ticket.getSurchargeForKM());
        ticket = saveAgeSurcharge(ticket);
        ticket = saveSurchargeForDelay(ticket);
        ticket = saveDiscountByRepairs(ticket);
        ticket = saveDiscountByDay(ticket);
        ticket = saveBonusBrand(ticket);
        ticket = saveTotalPrice(ticket);
        return ticketRepository.save(ticket);
    }


    // HU 4: Show info from repairs

    // i want to get the values from the vehicle
    public void getVehicleData(TicketEntity ticket){
        System.out.println("ESTOY CONSIGUIENDO LA DATA DEL VEHICULO");
        if (ticket == null) {
            return;
        }
        Long licensePlate = ticket.getLicensePlate();
        Vehicle vehicle = restTemplate.getForObject("http://gateway-server-service:8080/api/v1/vehicle/" + licensePlate, Vehicle.class);
        if (vehicle == null) {
            System.out.println("Vehicle is null");
            return;
        }
        ticket.setModel(vehicle.getModel());
        ticket.setBrand(vehicle.getBrand());
        ticket.setVehicleType(vehicle.getVehicleType());
        ticket.setYear(vehicle.getYear());
        System.out.println("Model: " + ticket.getModel());
        System.out.println("Brand: " + ticket.getBrand());
        System.out.println("Vehicle type: " + ticket.getVehicleType());
        System.out.println("Year: " + ticket.getYear());
        ticketRepository.save(ticket);
    }
    // idem but using repairs
    public void getRepairData(TicketEntity ticket){
        if (ticket == null) {
            return;
        }
        Long ticketId = ticket.getIdTicket();
        ResponseEntity<List<Repair>> response = restTemplate.exchange(
                "http://gateway-server-service:8080/api/v1/repair/byticket/" + ticketId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Repair>>() {}
        );
        List<Repair> repairs = response.getBody();
        if (repairs == null || repairs.isEmpty()) {
            return;
        }
        int totalSurcharges = 0;
        int totalDiscounts = 0;
        int subTotal = 0;
        RestTemplate restTemplate = new RestTemplate();

        for (Repair repair : repairs) {
            // Calculate the total surcharges and discounts in each repair using a request
            double percentage = 0.2; // replace with the actual percentage value

            HttpEntity<Repair> requestEntity = new HttpEntity<>(repair);

            // Calculate KM Surcharge
            totalSurcharges += restTemplate.exchange(
                    "http://gateway-server-service:8080/api/v1/repair/calculateKMSurcharge/" + percentage,
                    HttpMethod.PUT, requestEntity, Integer.class).getBody();

            // Calculate Age Surcharge
            totalSurcharges += restTemplate.exchange(
                    "http://gateway-server-service:8080/api/v1/repair/calculateAgeSurcharge/" + percentage,
                    HttpMethod.PUT, requestEntity, Integer.class).getBody();

            // Calculate Delay Surcharge
            totalSurcharges += restTemplate.exchange(
                    "http://gateway-server-service:8080/api/v1/repair/calculateDelaySurcharge/" + percentage,
                    HttpMethod.PUT, requestEntity, Integer.class).getBody();

            // Calculate Repairs Discount
            totalSurcharges -= restTemplate.exchange(
                    "http://gateway-server-service:8080/api/v1/repair/calculateRepairsDiscount/" + percentage,
                    HttpMethod.PUT, requestEntity, Integer.class).getBody();

            // Calculate Day Discount
            totalSurcharges -= restTemplate.exchange(
                    "http://gateway-server-service:8080/api/v1/repair/calculateDayDiscount/" + percentage,
                    HttpMethod.PUT, requestEntity, Integer.class).getBody();

            subTotal += repair.getBasePrice() + totalSurcharges;
        }
        System.out.println("YA PASAMOS LOS SURCHARGES");
        totalDiscounts = (int) (ticket.getDiscountForRepairs() + ticket.getDiscountPerDay());
        ticket.setTotalSurcharges(totalSurcharges);
        ticket.setTotalDiscounts(totalDiscounts);
        subTotal = subTotal - totalDiscounts;
        ticket.setSubTotal(subTotal);
        ticket.setIvaValue((int) Math.round(subTotal * 0.19));
        int totalPrice = subTotal + ticket.getIvaValue();
        ticket.setTotalPrice(totalPrice);
        System.out.println("Subtotal: " + ticket.getSubTotal());
        System.out.println("Discounts: " + ticket.getTotalDiscounts());
        System.out.println("surcharges: " + ticket.getTotalSurcharges());
        System.out.println("IVA: " + ticket.getIvaValue());
        System.out.println("Total price: " + ticket.getTotalPrice());
        ticketRepository.save(ticket);
    }

    public Vehicle getVehicleByLicensePlate(Long licensePlate){
        return restTemplate.getForObject("http://gateway-server-service:8080/api/v1/vehicle/" + licensePlate, Vehicle.class);
    }


    // HU 5: Show info from repairs per type of vehicles
    // I need to get the amount of repairs per type of vehicle using rest template requests

    public List<Integer> getTotalFromAType(int vehicleType, int repairType){
        List<TicketEntity> tickets = ticketRepository.findTicketsByVehicleType(vehicleType);
        List<Integer> result = new ArrayList<>();
        result.add(vehicleType);
        result.add(repairType);
        int totalPrice = 0;
        int totalCount = 0;
        if (tickets == null) {
            result.add(0);
            result.add(0);
            return result;
        }
        for (TicketEntity ticket : tickets) {
            Long ticketId = ticket.getIdTicket();
            if (ticketId != null) {
                ResponseEntity<Integer> response = restTemplate.exchange(
                        "http://gateway-server-service:8080/api/v1/repair/totalPrice/" + ticketId + "/" + repairType,
                        HttpMethod.GET,
                        null,
                        Integer.class
                );
                totalPrice += response.getBody();
                totalCount++;
            }
        }
        result.add(totalCount);
        result.add(totalPrice);
        return result;
    }
    // I need to use getTotalFromAType to get the values for each type of vehicle, the values are between 0 and 11
    public List<List<Integer>> getTotalFromAllTypes(){
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            for (int j = 1; j <= 11; j++) {
                result.add(getTotalFromAType(i, j));
            }

        }
        return result;
    }



}