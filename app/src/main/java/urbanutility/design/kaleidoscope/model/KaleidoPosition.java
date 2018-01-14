package urbanutility.design.kaleidoscope.model;

import urbanutility.design.kaleidoscope.datatypes.PositionType;

/**
 * Created by jerhan on 1/11/2018.
 */

public class KaleidoPosition {
    private String symbol;
    private long cost;
    private long costPerUnit;
    private long currentVal;
    private long realizedGain;
    private long unrealizedGain;
    private long amount;

    public KaleidoPosition(PositionType data){
        this.symbol = data.symbol;
        this.cost = data.cost;
        this.costPerUnit = data.costPerUnit;
        this.currentVal = data.currentVal;
        this.realizedGain = data.realizedGain;
        this.unrealizedGain = data.unrealizedGain;
        this.amount = data.amount;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public long getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(long costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public long getCurrentVal() {
        return currentVal;
    }

    public void setCurrentVal(long currentVal) {
        this.currentVal = currentVal;
    }

    public long getRealizedGain() {
        return realizedGain;
    }

    public void setRealizedGain(long realizedGain) {
        this.realizedGain = realizedGain;
    }

    public long getUnrealizedGain() {
        return unrealizedGain;
    }

    public void setUnrealizedGain(long unrealizedGain) {
        this.unrealizedGain = unrealizedGain;
    }
}

