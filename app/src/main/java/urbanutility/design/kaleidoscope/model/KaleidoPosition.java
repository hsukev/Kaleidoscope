package urbanutility.design.kaleidoscope.model;

/**
 * Created by jerye on 3/7/2018.
 */

public class KaleidoPosition {
    private String symbol;
    private double totalGain;
    private double avgUnitPrice;
    private double currentUnitPrice;
    private double realizedGain;
    private double unrealizedGain;
    private double amount;
    private double percentChange;

    public KaleidoPosition(String symbol, double totalGain, double avgUnitPrice, double currentUnitPrice, double realizedGain, double unrealizedGain, double amount, double percentChange) {
        this.symbol = symbol;
        this.totalGain = totalGain;
        this.avgUnitPrice = avgUnitPrice;
        this.currentUnitPrice = currentUnitPrice;
        this.realizedGain = realizedGain;
        this.unrealizedGain = unrealizedGain;
        this.amount = amount;
        this.percentChange = percentChange;
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

    public double getAvgUnitPrice() {
        return avgUnitPrice;
    }

    public void setAvgUnitPrice(double avgUnitPrice) {
        this.avgUnitPrice = avgUnitPrice;
    }

    public double getCurrentUnitPrice() {
        return currentUnitPrice;
    }

    public void setCurrentUnitPrice(double currentUnitPrice) {
        this.currentUnitPrice = currentUnitPrice;
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

    public double getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(double percentChange) {
        this.percentChange = percentChange;
    }
}
