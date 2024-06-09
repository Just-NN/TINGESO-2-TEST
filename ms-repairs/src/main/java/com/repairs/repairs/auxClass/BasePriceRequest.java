package com.repairs.repairs.auxClass;

import com.repairs.repairs.entities.RepairEntity;

public class BasePriceRequest {
    private RepairEntity repair;
    private int engineType;

    public RepairEntity getRepair() {
        return repair;
    }

    public void setRepair(RepairEntity repair) {
        this.repair = repair;
    }

    public int getEngineType() {
        return engineType;
    }

    public void setEngineType(int engineType) {
        this.engineType = engineType;
    }
}
