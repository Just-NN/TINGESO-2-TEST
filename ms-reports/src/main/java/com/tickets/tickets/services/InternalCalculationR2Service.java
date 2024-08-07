package com.tickets.tickets.services;

import com.tickets.tickets.entities.MonthRepairEntity;
import com.tickets.tickets.entities.R1Entity;
import com.tickets.tickets.entities.VehicleRepairEntity;
import com.tickets.tickets.repositories.MonthRepairRepository;
import com.tickets.tickets.repositories.VehicleRepairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class InternalCalculationR2Service {


    @Autowired
    MonthRepairRepository monthRepairRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private MonthRepairService monthRepairService;

    // Method to count each type of repair taking a list of integers as parameter



    public List<Long> calculateR2(int month, int year) {
        List<MonthRepairEntity> monthRepairs = new ArrayList<>();
        // Convert month and year to LocalDate, then format to string
        LocalDate date = LocalDate.of(year, month, 1);
        String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println("Converted date to string: " + dateString);

        // Construct the URL with the formatted date string
        String urlTemplate = UriComponentsBuilder.fromHttpUrl("http://gateway-server-service:8080/api/v1/ticket/repairs/totalFromAllTypesPerMonth/" + month + "/" + year)
                .toUriString();
        System.out.println("URL constructed");

        // Make the HTTP GET request
        ResponseEntity<List<List<Integer>>> response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<List<Integer>>>() {}
        );
        System.out.println("Response: " + response.toString());
        List<List<Integer>> repairs = response.getBody();

        // Reset all vehicle repairs in the database to 0
        monthRepairService.resetValues();
        System.out.println("All values reset");
        System.out.println("Repairs: " + repairs);

        if (repairs == null) {
            System.out.println("Repairs are null");
            monthRepairs = createMonthRepairs();
        }

        else{
            for (List<Integer> repair : repairs) {
                // Must look what repair type is and then what vehicle type is
                System.out.println("Repair: " + repair);
                // Look if there's a month repair entity with the same repair type
                MonthRepairEntity monthRepair = monthRepairRepository.findMonthRepairEntitiesByRepairType(repair.get(0)).get(0);
                System.out.println("FOUND ONE: " + monthRepair);
                // if not, create a new one
                if (monthRepair == null) {
                    System.out.println("Month repair is null");
                    monthRepair = new MonthRepairEntity();
                    monthRepair.setRepairType(repair.get(0));
                }
                // Then get the lists with the same repair type
                List<List<Integer>> repairLists = seekLists(repairs, repair.get(0));
                List<Integer> sortedMonths = sortMonths(month);
                System.out.println("Sorted months: " + sortedMonths);
                int currentAmount = sumCounts(repairLists, sortedMonths.get(2));
                int previousAmount = sumCounts(repairLists, sortedMonths.get(1));
                int previousPreviousAmount = sumCounts(repairLists, sortedMonths.get(0));
                int currentPrice = sumPrices(repairLists, sortedMonths.get(2));
                int previousPrice = sumPrices(repairLists, sortedMonths.get(1));
                int previousPreviousPrice = sumPrices(repairLists, sortedMonths.get(0));

                System.out.println("Sorted months: " + sortedMonths);
                setMonthValues(monthRepair, sortedMonths.get(0), month, previousPreviousAmount, previousPreviousPrice);
                setMonthValues(monthRepair, sortedMonths.get(1), month, previousAmount, previousPrice);
                setMonthValues(monthRepair, sortedMonths.get(2), month, currentAmount, currentPrice);
                monthRepairRepository.save(monthRepair);
                System.out.println("Month repair saved: " + monthRepair);
            }
            monthRepairs = monthRepairRepository.findAllMonthRepairs();
        }

        List<Long> repairIds = new ArrayList<>();
        monthRepairRepository.saveAll(monthRepairs);
        System.out.println("Month repairs saved: " + monthRepairs);

        for (int i = 1; i < 13; i++) {
            repairIds.add((long) i);
        }
        System.out.println("Repair ids: " + repairIds);
        return repairIds;
    }

    private List<MonthRepairEntity> createMonthRepairs() {
        List<MonthRepairEntity> monthRepairs = new ArrayList<>();
        for (int i = 1; i < 13; i++){
            MonthRepairEntity monthRepair = new MonthRepairEntity();
            monthRepair.setRepairType(i);
            monthRepairs.add(monthRepair);
        }
        return monthRepairs;
    }



    // Sort months in order to determine the current and the previous
    public List<Integer> sortMonths(int month){
        List<Integer> sortedMonths = new ArrayList<>();
        if (month == 1){
            sortedMonths.add(1);
            sortedMonths.add(12);
            sortedMonths.add(1);
        }
        else {
            sortedMonths.add(month - 2);
            sortedMonths.add(month - 1);
            sortedMonths.add(month);
        }
        return sortedMonths;
    }
    // Set the values of the month repair entity
    public void setMonthValues(MonthRepairEntity monthRepair, int month, int currentMonth, int amount, int price){
        if (month == currentMonth){
            monthRepair.setThirdMonth(month);
            monthRepair.setThirdMonthAmount(amount);
            monthRepair.setThirdMonthPrice(price);
        }
        else if (month == currentMonth - 2){
            monthRepair.setFirstMonth(month);
            monthRepair.setFirstMonthAmount(amount);
            monthRepair.setFirstMonthPrice(price);
        }
        else if (month == currentMonth - 1){
            monthRepair.setSecondMonth(month);
            monthRepair.setSecondMonthAmount(amount);
            monthRepair.setSecondMonthPrice(price);
        }
    }

    // Seek the lists with the same repair type
    public List<List<Integer>> seekLists(List<List<Integer>> repairs, int repairType){
        List<List<Integer>> repairLists = new ArrayList<>();
        for (List<Integer> repair: repairs){
            if (repair.get(0) == repairType){
                repairLists.add(repair);
            }
        }
        return repairLists;
    }
    // Get the count of the lists that has the same repair type and month
    public int sumCounts(List<List<Integer>> repairLists, int month){
        int totalCount = 0;
        for (List<Integer> repair: repairLists){
            if (repair.get(3) == month){
                totalCount += repair.get(1);
            }
        }
        return totalCount;
    }
    // Get the price of the lists that has the same repair type and month
    public int sumPrices(List<List<Integer>> repairLists, int month){
        int totalPrice = 0;
        for (List<Integer> repair: repairLists){
            if (repair.get(3) == month){
                totalPrice += repair.get(2);
            }
        }
        return totalPrice;
    }

}

// MUST CREATE THE MICROSERVICES AND CONNECT THEM TO THIS ONE TO GET THE DATA AND WORK WITH IT
