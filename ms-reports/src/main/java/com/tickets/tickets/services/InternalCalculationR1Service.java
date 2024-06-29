package com.tickets.tickets.services;

import com.tickets.tickets.entities.VehicleRepairEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class InternalCalculationR1Service {


    @Autowired
    RestTemplate restTemplate;

    // Method to count each type of repair taking a list of integers as parameter

    public int[] countEachTypeOfRepair(List<Integer> repairTypes) {
        if (repairTypes == null) {
            return null;
        }
        int[] repairCounts = new int[12];
        for (int repairType : repairTypes) {
            if (repairType >= 1 && repairType <= 12) {
                repairCounts[repairType - 1]++;
            }
        }
        return repairCounts;
    }





}

// MUST CREATE THE MICROSERVICES AND CONNECT THEM TO THIS ONE TO GET THE DATA AND WORK WITH IT
