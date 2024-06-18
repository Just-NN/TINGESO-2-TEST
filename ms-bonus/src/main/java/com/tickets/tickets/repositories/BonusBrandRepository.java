package com.tickets.tickets.repositories;

import com.tickets.tickets.entities.BonusBrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface BonusBrandRepository extends JpaRepository<BonusBrandEntity, Long> {
    BonusBrandEntity findBonusBrandByIdBonus(Long id);

    @Query(value = "SELECT * FROM bonusbrand b WHERE b.brand = :brand AND b.active = true ORDER BY b.bonus DESC LIMIT 1", nativeQuery = true)
    List<BonusBrandEntity> findHighestActiveBonusByBrand(@Param("brand") String brand);
}