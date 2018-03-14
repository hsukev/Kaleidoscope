package urbanutility.design.kaleidoscope.model;

/**
 * Created by jerye on 3/7/2018.
 */

public class KaleidoPosition {
    private String symbol;
    private double totalGain;
    private double costPerUnit;
    private double currentVal;
    private double realizedGain;
    private double unrealizedGain;
    private double amount;
    private double changePercent;

    public KaleidoPosition(String symbol, double totalGain, double costPerUnit, double currentVal, double realizedGain, double unrealizedGain, double amount, double changePercent) {
        this.symbol = symbol;
        this.totalGain = totalGain;
        this.costPerUnit = costPerUnit;
        this.currentVal = currentVal;
        this.realizedGain = realizedGain;
        this.unrealizedGain = unrealizedGain;
        this.amount = amount;
        this.changePercent = changePercent;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getTotalGain() {
        return totalGain;
    }

    public void setTotalGain(double totalGain) {
        this.totalGain = totalGain;
    }

    public double getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(double costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public double getCurrentVal() {
        return currentVal;
    }

    public void setCurrentVal(double currentVal) {
        this.currentVal = currentVal;
    }

    public double getRealizedGain() {
        return realizedGain;
    }

    public void setRealizedGain(double realizedGain) {
        this.realizedGain = realizedGain;
    }

    public double getUnrealizedGain() {
        return unrealizedGain;
    }

    public void setUnrealizedGain(double unrealizedGain) {
        this.unrealizedGain = unrealizedGain;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }
}
