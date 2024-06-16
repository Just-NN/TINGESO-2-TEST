package usach.tingeso.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import usach.tingeso.entities.BonusBrandEntity;
import usach.tingeso.repositories.BonusBrandRepository;

import java.util.List;

@Service
public class BonusBrandService {
    @Autowired
    BonusBrandRepository bonusBrandRepository;

    // Basic CRUD operations
    public BonusBrandEntity getBonusBrandById(Long id){
        if (bonusBrandRepository.findBonusBrandByIdBonus(id) == null)
            return null;
        return bonusBrandRepository.findBonusBrandByIdBonus(id);
    }
    public List<BonusBrandEntity> getAllBonusBrands(){
        List<BonusBrandEntity> bonusBrands = bonusBrandRepository.findAll();
        if (bonusBrands.isEmpty())
            return null;
        return bonusBrands;
    }
    public BonusBrandEntity saveBonusBrand(BonusBrandEntity bonusBrand){
        if (bonusBrand == null)
            return null;
        return bonusBrandRepository.save(bonusBrand);
    }
    public void deleteBonusBrand(Long id){
        if (bonusBrandRepository.findBonusBrandByIdBonus(id) == null)
            return;
        bonusBrandRepository.deleteById(id);
    }
    public BonusBrandEntity updateBonusBrand(BonusBrandEntity bonusBrand){
        if (bonusBrand == null)
            return null;
        return bonusBrandRepository.save(bonusBrand);
    }

    // Get the bonus by brand
    public int getBonusByBrand(String brand){
        if (brand == null || brand.isEmpty())
            return 0;
        List<BonusBrandEntity> bonusBrandEntities = bonusBrandRepository.findHighestActiveBonusByBrand(brand);
        if (bonusBrandEntities.isEmpty())
            return 0;
        return bonusBrandEntities.get(0).getBonus();
    }

    // find the highest bonus by brand active
    public List<BonusBrandEntity> findHighestActiveBonusByBrand(String brand){
        if (brand == null || brand.isEmpty())
            return null;
        return bonusBrandRepository.findHighestActiveBonusByBrand(brand);
    }
    // EOF
}