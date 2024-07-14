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

import java.util.ArrayList;
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



    public List<Long> calculateR2(){
        String url = "http://gateway-server-service:8080/api/v1/ticket/repairs/totalFromAllTypesPerMonth";
        ResponseEntity<List<List<Integer>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<List<Integer>>>() {}
        );
        List<List<Integer>> repairs = response.getBody();
        // First, I must set all the vehicle repairs in the database to 0
        monthRepairService.resetValues();
        System.out.println("All values reset");
        System.out.println("Repairs: " + repairs.toString());
        // The first element is the repair type
        // The second element is the amount of repairs
        // The third element is the price of the repair
        // The fourth element is the month of the repair
        // Now is important to get each value and set them to MonthRepairEntity
        // I need the conditionals to set the values
        if (repairs == null){
            System.out.println("Repairs are null");
            return null;
        }

        for(List<Integer> repair: repairs){
            System.out.println("Repair: " + repair.toString());
            // Must look what repair type is and then what vehicle type is
            MonthRepairEntity monthRepair = monthRepairRepository.findMonthRepairEntityByRepairType(repair.get(0));
            if (monthRepair == null){
                System.out.println("Month repair is null");
                monthRepair = new MonthRepairEntity();
                monthRepair.setRepairType(repair.get(0));
            }
            int month = repair.get(3);
            // Once i have the values, i must seek the other 2 lists that have the same repair type
            List<List<Integer>> repairLists = seekLists(repairs, repair.get(0));
            // Now i must sort the months to determine the current and the previous
            List<Integer> sortedMonths = sortMonths(month, repairLists.get(1).get(3), repairLists.get(2).get(3));
            // Now i must calculate the 3 counts of the repair type with the same month for each month
            int currentAmount = sumCounts(repairLists, sortedMonths.get(2));
            int previousAmount = sumCounts(repairLists, sortedMonths.get(1));
            int previousPreviousAmount = sumCounts(repairLists, sortedMonths.get(0));
            // Now i must calculate the 3 prices of the repair type with the same month for each month
            int currentPrice = sumPrices(repairLists, sortedMonths.get(2));
            int previousPrice = sumPrices(repairLists, sortedMonths.get(1));
            int previousPreviousPrice = sumPrices(repairLists, sortedMonths.get(0));

            // Now i must set the values of each month in my entity
            setMonthValues(monthRepair, sortedMonths.get(0), previousPreviousAmount, previousPreviousPrice);
            setMonthValues(monthRepair, sortedMonths.get(1), previousAmount, previousPrice);
            setMonthValues(monthRepair, sortedMonths.get(2), currentAmount, currentPrice);
            // And save
            monthRepairRepository.save(monthRepair);
            System.out.println("Month repair saved");
            System.out.println(monthRepair.toString());

            }
            List<MonthRepairEntity> monthRepairs = monthRepairRepository.findAllMonthRepairs();
            if (monthRepairs == null){
                monthRepairs = createMonthRepairs();
            }
            // return long list from 0 to 11
            List<Long> repairIds = new ArrayList<>();
            monthRepairRepository.saveAll(monthRepairs);
            System.out.println("Month repairs: " + monthRepairs.toString());
            // filling the list with the ids from 0 to 11
            for (int i = 0; i < 12; i++){
                repairIds.add((long) i);
            }
            System.out.println("Repair ids: " + repairIds.toString());
            return repairIds;
        }

    private List<MonthRepairEntity> createMonthRepairs() {
        List<MonthRepairEntity> monthRepairs = new ArrayList<>();
        for (int i = 0; i < 12; i++){
            MonthRepairEntity monthRepair = new MonthRepairEntity();
            monthRepair.setRepairType(i);
            monthRepairs.add(monthRepair);
        }
        return monthRepairs;
    }



        // Sort months in order to determine the current and the previous
        public List<Integer> sortMonths(int month1, int month2, int month3){

            // sort months from lowest to highest
            int[] months = {month1, month2, month3};
            int temp;
            for (int i = 0; i < months.length; i++){
                for (int j = i + 1; j < months.length; j++){
                    if (months[i] > months[j]){
                        temp = months[i];
                        months[i] = months[j];
                        months[j] = temp;
                    }
                }
            }
            return List.of(months[0], months[1], months[2]);
        }
        // Set the values of the month repair entity
        public void setMonthValues(MonthRepairEntity monthRepair, int month, int amount, int price){
            if (month == 1){
                monthRepair.setFirstMonth(amount);
                monthRepair.setFirstMonthAmount(amount);
                monthRepair.setFirstMonthPrice(price);
            }
            if (month == 2){
                monthRepair.setSecondMonth(amount);
                monthRepair.setSecondMonthAmount(amount);
                monthRepair.setSecondMonthPrice(price);
            }
            if (month == 3){
                monthRepair.setThirdMonth(amount);
                monthRepair.setThirdMonthAmount(amount);
                monthRepair.setThirdMonthPrice(price);
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
