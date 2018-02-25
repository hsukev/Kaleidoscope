package urbanutility.design.kaleidoscope.model;

/**
 * Created by jerye on 2/16/2018.
 */

public class KaleidoDeposits {
    String side;
    String exchange;
    double amount;
    String symbol;
    double commission;

    public KaleidoDeposits(String side, String exchange, double amount, String symbol, double commission) {
        this.side = side;
        this.exchange = exchange;
        this.amount = amount;
        this.symbol = symbol;
        this.commission = commission;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }
}
