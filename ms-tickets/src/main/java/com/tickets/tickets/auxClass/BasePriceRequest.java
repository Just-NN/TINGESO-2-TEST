package com.tickets.tickets.auxClass;

import com.tickets.tickets.models.Repair;

public class BasePriceRequest {
    private Repair repair;
    private int engineType;

    public Repair getRepair() {
        return repair;
    }

    public void setRepair(Repair repair) {
        this.repair = repair;
    }

    public int getEngineType() {
        return engineType;
    }

    public void setEngineType(int engineType) {
        this.engineType = engineType;
    }
}
