package urbanutility.design.kaleidoscope.datatypes;

/**
 * Created by Jason on 1/13/2018.
 */

/* Used for storing raw balance data from API */
public class BalanceType {
    public String symbol;
    public String exchange;
    public double amount;

    public BalanceType(String symbol, String exchange, double amount) {
        this.symbol = symbol;
        this.exchange = exchange;
        this.amount = amount;
    }



}

