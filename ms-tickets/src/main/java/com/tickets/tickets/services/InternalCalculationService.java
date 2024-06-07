package com.tickets.tickets.services;

import com.tickets.tickets.clients.RepairFeignClient;
import com.tickets.tickets.clients.VehicleFeignClient;
import com.tickets.tickets.entities.TicketEntity;
import com.tickets.tickets.models.Repair;
import com.tickets.tickets.models.Vehicle;
import com.tickets.tickets.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

@Service
public class InternalCalculationService {


    @Autowired
    private TicketRepository ticketRepository;

    private VehicleFeignClient vehicleFeignClient;

    private RepairFeignClient repairFeignClient;



    // This is the service that will calculate the final price for the repair


    // Calculate the surcharge for the kilometers
    public double calculateSurchargeForKM(TicketEntity ticket) {
        // Null case
        if (ticket == null) {
            return -1;
        }

        // Get the license plate from the ticket
        Long licensePlate = ticket.getLicensePlate();

        Vehicle vehicle = vehicleFeignClient.getVehicleById(licensePlate);
        if (vehicle == null) {
            return -1;
        }
        int engineType = vehicle.getEngineType();
        double surchargeKM = 0;
        int mileage = vehicle.getMileage();
        int vehicleType = vehicle.getVehicleType();

        // Define a 2D array to store the surcharge rates
        double[][] surchargeRates = {
                {0.03, 0.07, 0.12, 0.2}, // Sedan/Hatchback
                {0.03, 0.07, 0.12, 0.2}, // Sedan/Hatchback
                {0.05, 0.09, 0.12, 0.2}, // SUV/Pickup/Van
                {0.05, 0.09, 0.12, 0.2}, // SUV/Pickup/Van
                {0.05, 0.09, 0.12, 0.2}  // SUV/Pickup/Van
        };

        // Check if the mileage is within the valid range
        if (mileage > 5000) {
            int mileageIndex;
            if (mileage < 12000) {
                mileageIndex = 0;
            } else if (mileage < 25000) {
                mileageIndex = 1;
            } else if (mileage < 40000) {
                mileageIndex = 2;
            } else {
                mileageIndex = 3;
            }

            // Calculate the surcharge rate from the array
            surchargeKM = surchargeRates[vehicleType][mileageIndex];
        } else if (mileage < 0) {
            surchargeKM = -1;
        }

        // Save the surcharge in the RepairEntity
        ticket.setSurchargeForKM(surchargeKM);
        ticketRepository.save(ticket);
        return surchargeKM;
    }
    public double calculateSurchargeByAge(TicketEntity ticket){
        {
            if (ticket == null) {
                return -1;
            }
            Long licensePlate = ticket.getLicensePlate();
            Vehicle vehicle = vehicleFeignClient.getVehicleById(licensePlate);
            if (vehicle == null) {
                return -1;
            }
            int engineType = vehicle.getEngineType();
            double ageSurcharge = 0;
            int age = Calendar.getInstance().get(Calendar.YEAR) - vehicle.getYear();
            int type = vehicle.getVehicleType();

            // Define a 2D array to store the surcharge rates
            double[][] surchargeRates = {
                    {0.05, 0.09, 0.15}, // Sedan/Hatchback
                    {0.05, 0.09, 0.15}, // Sedan/Hatchback
                    {0.07, 0.11, 0.2},  // SUV/Pickup/Van
                    {0.07, 0.11, 0.2},  // SUV/Pickup/Van
                    {0.07, 0.11, 0.2}   // SUV/Pickup/Van
            };

            if (age < 0) {
                ageSurcharge = -1;
            } else if (age > 5) {
                int ageIndex;
                if (age < 10) {
                    ageIndex = 0;
                } else if (age < 15) {
                    ageIndex = 1;
                } else {
                    ageIndex = 2;
                }

                // Calculate the surcharge rate from the array
                ageSurcharge = surchargeRates[type][ageIndex];
            }

            // Save the surcharge in the RepairEntity
            ticket.setSurchargeForAge(ageSurcharge);
            ticketRepository.save(ticket);
            return ageSurcharge;
        }
    }
    // Calculate the surcharge for the delay

    public double calculateSurchargeForDelay(TicketEntity ticket){
        if (ticket == null) {
            return -1;
        }
        long delay = ChronoUnit.DAYS.between(ticket.getEntryDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                ticket.getPickupDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        // This is to prevent round errors
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        double delaySurcharge = Double.valueOf(df.format(-(delay)*0.05));
        System.out.println("Delay: " + delaySurcharge);
        // Save the surcharge in the TicketEntity
        ticket.setSurchargeForDelay(delaySurcharge);
        ticketRepository.save(ticket);
        return delaySurcharge;
    }
    // Calculate the number of repairs this year
    public int calculateRepairsThisYear(TicketEntity ticket){
        if (ticket == null) {
            return -1;
        }
        Long licensePlate = ticket.getLicensePlate();
        // get all the tickets this 12 months using findTicketsByVehicleThisYear
        List<TicketEntity> tickets = ticketRepository.findTicketsByVehicleThisYear(licensePlate, ticket.getEntryDate());
        if (tickets == null) {
            return -1;
        }
        int repairQuantity = 0;
        // Now we must count the number of repairs using a for on tickets
        for (TicketEntity t : tickets) {
            List<Repair> repairs = repairFeignClient.getRepairsByTicket(t.getIdTicket());
            if (repairs == null) {
                return -1;
            }
            // get the size of the list and add it to repairQuantity
            repairQuantity += repairs.size();
        }
        return tickets.size();
    }

    // Calculate the discount by the number of repairs
    public double calculateDiscountByRepairs(TicketEntity ticket){
        if (ticket == null) {
            return -1;
        }
        Long licensePlate = ticket.getLicensePlate();
        // MUST change the query for a ms getter
        Vehicle vehicle = vehicleFeignClient.getVehicleById(licensePlate);
        System.out.println("Vehicle: " + vehicle);
        if (vehicle == null) {
            return -1;
        }
        int repairCount = calculateRepairsThisYear(ticket);
        int engineType = vehicle.getEngineType();

        // Define a 2D array to store the discount rates
        double[][] discountRates = {
                {0.05, 0.10, 0.15, 0.2},  // Gasoline
                {0.07, 0.12, 0.17, 0.22}, // Diesel
                {0.10, 0.15, 0.20, 0.25}, // Hybrid
                {0.08, 0.13, 0.18, 0.23}  // Electric
        };

        double discount = 0;
        if (repairCount > 0){
            int repairIndex;
            if (repairCount < 3) {
                repairIndex = 0;
            } else if (repairCount < 6) {
                repairIndex = 1;
            } else if (repairCount < 10) {
                repairIndex = 2;
            } else {
                repairIndex = 3;
            }

            // Calculate the discount rate from the array
            discount = discountRates[engineType][repairIndex];
            System.out.println("Discount: " + discount);
        }

        // Save the discount in the RepairEntity
        ticket.setDiscountForRepairs(discount);
        ticketRepository.save(ticket);
        return discount;
    }

    // Calculate the discount by the day of the week

    public double calculateDiscountByDay(TicketEntity ticket){
        if (ticket == null) {
            return -1;
        }
        ZonedDateTime entryDateTime = ticket.getEntryDate().toInstant().atZone(ZoneId.systemDefault());
        int entryDay = entryDateTime.getDayOfWeek().getValue();
        System.out.println("Day: " + entryDay);
        int entryHour = entryDateTime.getHour();
        System.out.println("Hour: " + entryHour);
        double dayDiscount = 0;
        if (entryDay >= 1 && entryDay <= 4){ // 1 = Monday, 4 = Thursday in java.time API
            System.out.println("Es buen dia");
            if (entryHour >= 9 && entryHour <= 12){
                System.out.println("Es buen horario");
                dayDiscount = 0.1;
            }
        }

        // Save the discount in the ticket
        ticket.setDiscountPerDay(dayDiscount);
        ticketRepository.save(ticket);
        return dayDiscount;
    }

    // Calculate the total price for the ticket
    public double calculateTotalPrice(TicketEntity ticket){
        if (ticket == null) {
            return -1;
        }
        double kmSurcharge = ticket.getSurchargeForKM();
        double ageSurcharge = ticket.getSurchargeForAge();
        double delaySurcharge = ticket.getSurchargeForDelay();
        double repairDiscount = ticket.getDiscountForRepairs();
        double dayDiscount = ticket.getDiscountPerDay();
        double finalPrice = 0;
        if (kmSurcharge < 0 || ageSurcharge < 0 || repairDiscount < 0){
            return -1;
        }
        else{
            finalPrice = (ticket.getBasePrice() + (kmSurcharge + ageSurcharge + delaySurcharge) - (repairDiscount + dayDiscount))*1.19;
        }
        return finalPrice;
    }

}

// MUST CREATE THE MICROSERVICES AND CONNECT THEM TO THIS ONE TO GET THE DATA AND WORK WITH IT
