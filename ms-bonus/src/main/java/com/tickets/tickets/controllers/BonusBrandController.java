package com.tickets.tickets.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tickets.tickets.entities.BonusBrandEntity;
import com.tickets.tickets.services.BonusBrandService;

import java.util.List;

@RestController
@RequestMapping("api/v1/bonusBrand")
public class BonusBrandController {

    @Autowired
    BonusBrandService bonusBrandService;

    @GetMapping("/{id}")
    public ResponseEntity<BonusBrandEntity> getBonusBrandById(@PathVariable Long id){
        BonusBrandEntity bonusBrand = bonusBrandService.getBonusBrandById(id);
        if (bonusBrand == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(bonusBrand);
    }
    @GetMapping("/")
    public ResponseEntity<Iterable<BonusBrandEntity>> getAllBonusBrands(){
        Iterable<BonusBrandEntity> bonusBrands = bonusBrandService.getAllBonusBrands();
        if (bonusBrands == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(bonusBrands);
    }

    @PostMapping("/")
    public ResponseEntity<BonusBrandEntity> saveBonusBrand(@RequestBody BonusBrandEntity bonusBrand){
        if (bonusBrand == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(bonusBrandService.saveBonusBrand(bonusBrand));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBonusBrand(@PathVariable Long id){
        BonusBrandEntity bonusBrand = bonusBrandService.getBonusBrandById(id);
        if (bonusBrand == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bonusBrandService.deleteBonusBrand(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<BonusBrandEntity> updateBonusBrand(@RequestBody BonusBrandEntity bonusBrand){
        if (bonusBrand == null || bonusBrandService.getBonusBrandById(bonusBrand.getIdBonus()) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(bonusBrandService.updateBonusBrand(bonusBrand));
    }

    @GetMapping("/bonus/{brand}")
    public ResponseEntity<Integer> getBonusByBrand(@PathVariable String brand){
        Integer bonus = bonusBrandService.getBonusByBrand(brand);
        return ResponseEntity.ok(bonus);
    }
    @GetMapping("/highestActive/{brand}/{idTicket}")
    public ResponseEntity<Integer> findHighestActiveBonusByBrand(@PathVariable String brand, @PathVariable Long idTicket){
        List<BonusBrandEntity> bonusBrandEntities = bonusBrandService.findHighestActiveBonusByBrand(brand);
        if (bonusBrandEntities == null || bonusBrandEntities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Assuming that the first entity in the list has the highest bonus
        BonusBrandEntity highestBonusBrandEntity = bonusBrandEntities.get(0);
        highestBonusBrandEntity.setActive(false); // set active to false
        highestBonusBrandEntity.setIdTicket(idTicket);
        System.out.println("Highest bonus: " + highestBonusBrandEntity.getBonus());
        System.out.println("Highest bonus id: " + highestBonusBrandEntity.getIdBonus());
        bonusBrandService.updateBonusBrand(highestBonusBrandEntity); // update the entity
        return ResponseEntity.ok(highestBonusBrandEntity.getBonus());
    }
}