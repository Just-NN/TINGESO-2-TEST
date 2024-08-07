package com.tickets.tickets.controllers;

import com.tickets.tickets.models.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tickets.tickets.entities.TicketEntity;
import com.tickets.tickets.services.TicketService;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/ticket")
public class TicketController {

    @Autowired
    TicketService ticketService;

    @GetMapping("/{id}")
    public ResponseEntity<TicketEntity> getTicketById(@PathVariable Long id){
        TicketEntity ticket = ticketService.getTicketById(id);
        if (ticket == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ticket);
    }

    @PostMapping("/")
    public ResponseEntity<TicketEntity> saveTicket(@RequestBody TicketEntity ticket){
        if (ticket == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        System.out.println("Save ticket");
        System.out.println(ticket.toString());
        return ResponseEntity.ok(ticketService.saveTicket(ticket));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id){
        TicketEntity ticket = ticketService.getTicketById(id);
        if (ticket == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ticketService.deleteTicket(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/")
    public ResponseEntity<TicketEntity> updateTicket(@RequestBody TicketEntity ticket){
        if (ticket == null || ticketService.getTicketById(ticket.getIdTicket()) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(ticketService.updateTicket(ticket));
    }

    @GetMapping("/")
    public ResponseEntity<List<TicketEntity>> getAllTickets(){
        List<TicketEntity> tickets = ticketService.getAllTickets();
        if (tickets == null || tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tickets);
    }

    //------------------------------------------------------------------------------------------------------------
    // Operations
    @PutMapping("/basePrice")
    public ResponseEntity<TicketEntity> saveBasePrice(@RequestBody Long id){
        TicketEntity ticket = ticketService.getTicketById(id);
        System.out.println("Base price");
        if (ticket == null) {
            System.out.println("Ticket is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(ticketService.saveBasePrice(ticket));
    }

    @PutMapping("/kmSurcharge")
    public ResponseEntity<TicketEntity> saveKMSurcharge(@RequestBody TicketEntity ticket){
        if (ticket == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(ticketService.saveKMSurcharge(ticket));
    }

    @PutMapping("/ageSurcharge")
    public ResponseEntity<TicketEntity> saveAgeSurcharge(@RequestBody TicketEntity ticket){
        if (ticket == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(ticketService.saveAgeSurcharge(ticket));
    }

    @PutMapping("/delaySurcharge")
    public ResponseEntity<TicketEntity> saveSurchargeForDelay(@RequestBody TicketEntity ticket){
        if (ticket == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(ticketService.saveSurchargeForDelay(ticket));
    }

    @PutMapping("/repairDiscount")
    public ResponseEntity<TicketEntity> saveDiscountByRepairs(@RequestBody TicketEntity ticket){
        if (ticket == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(ticketService.saveDiscountByRepairs(ticket));
    }

    @PutMapping("/dayDiscount")
    public ResponseEntity<TicketEntity> saveDiscountByDay(@RequestBody TicketEntity ticket){
        if (ticket == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(ticketService.saveDiscountByDay(ticket));
    }


    @PutMapping("/totalPrice")
    public ResponseEntity<TicketEntity> saveTotalPrice(@RequestBody TicketEntity ticket){
        if (ticket == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(ticketService.saveTotalPrice(ticket));
    }
    @PutMapping("/brandBonus")
    public ResponseEntity<TicketEntity> saveBrandBonus(@RequestBody TicketEntity ticket){
        if (ticket == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(ticketService.saveBonusBrand(ticket));
    }
    //------------------------------------------------------------------------------------------------------------
    // Get repair types
    @GetMapping("/repairTypes/{id}")
    public ResponseEntity<List<Integer>> getRepairTypes(@PathVariable Long id){
        List<Integer> repairTypes = ticketService.getTypeOfRepairs(id);
        if (repairTypes == null || repairTypes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(repairTypes);
    }
    // Initialization for the ticket
    @PutMapping(value = "/init", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TicketEntity> saveInit(@RequestBody TicketEntity ticket){
        System.out.println("Init ticket");
        if (ticket == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        System.out.println(ticket.toString());
        return ResponseEntity.ok(ticketService.saveTicketWithOperations(ticket));
    }
    // init values from vehicles and repairs for ticket
    @PutMapping(value = "/initValues", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TicketEntity> saveInitValues(@RequestBody TicketEntity ticket){
        System.out.println("Init values");
        if (ticket == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        System.out.println(ticket.toString());
        ticketService.getVehicleData(ticket);
        ticketService.getRepairData(ticket);
        System.out.println(ticket.toString());
        return ResponseEntity.ok(ticket);
    }

    // This is a resttemplate test controller
    @GetMapping("/getVehicle/{id}")
    public ResponseEntity<Vehicle> getVehicleData(@PathVariable Long id){
        Vehicle vehicle = ticketService.getVehicleByLicensePlate(id);
        if (vehicle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        System.out.println(vehicle.toString());
        return ResponseEntity.ok(vehicle);
    }

    // HU 5 Controller
    @GetMapping("/repairs/totalFromAllTypes")
    public List<List<Integer>> getTotalFromAllTypes() {
        return ticketService.getTotalFromAllTypes();
    }


    // HU 6 Controller - Get count and price per months
    @GetMapping("/repairs/totalFromAllTypesPerMonth/{month}/{year}")
    public ResponseEntity<List<List<Integer>>> getValuesByMonthController(@PathVariable int month, @PathVariable int year){
        List<List<Integer>> values = ticketService.getValuesByThreeMonths(month, year);
        if (values.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(values);
    }

}