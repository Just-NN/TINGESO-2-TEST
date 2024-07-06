package com.tickets.tickets.services;

import com.tickets.tickets.entities.R1Entity;
import com.tickets.tickets.repositories.R1Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class R1Service {

    @Autowired
    R1Repository r1Repository;
    @Autowired
    InternalCalculationR1Service internalCalculationR1Service;

    // Crud
    public R1Entity getR1ById(Long id){
        return r1Repository.findById(id).orElse(null);
    }
    public R1Entity saveR1(R1Entity r1) {
        if (r1 == null) {
            // Handle the case where r1 is null
            return null;
        }
        return r1Repository.save(r1);
    }
    public void deleteR1(Long id){
        if (r1Repository.existsById(id)) {
            r1Repository.deleteById(id);
        }
    }
    public R1Entity updateR1(R1Entity r1){
        if (r1Repository.existsById(r1.getIdR1())) {
            return r1Repository.save(r1);
        }
        return null;
    }

    public void deleteAllR1(){
        r1Repository.deleteAll();
    }

    // Initialize the values
    public void initializeValues(){
        R1Entity r1Entity = new R1Entity();
        List<Long> values = internalCalculationR1Service.calculateR1();
        if (values == null){
            return;
        }
        for (int i = 0; i < values.size(); i++){
            r1Entity.setVehicleRepairIds(values);
            r1Repository.save(r1Entity);
        }
        saveR1(r1Entity);
    }

    // create empty R1Entity and save it
    public void createEmptyR1(){
        R1Entity r1Entity = new R1Entity();
        System.out.println("Creating empty R1Entity");
        saveR1(r1Entity);
    }
}
