package urbanutility.design.kaleidoscope.datatypes;

/**
 * Created by Jason on 1/13/2018.
 */

public class OrderType {
    public String exchange; /* Binance, Kraken... */
    public String symbol;
    public String convertedSymbol;
    public String side; /* Buy or Sell */
    public double price;
    public double btcUsdRate;
    public double amount;
    public double btcPrice;
    public double txFee;
    public String time; /* ISO 8601 format: "yyyy-MM-dd'T'HH:mm:ssZ" */

    public OrderType () {

    }
}
