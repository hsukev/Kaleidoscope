package urbanutility.design.kaleidoscope.datatypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 1/13/2018.
 */

public class OrderType {
    public String id; /* For Database use only */
    public String exchange; /* Binance, Kraken... */
    public String symbol;
    public String type; /* Buy or Sell */
    public long price;
    public long btcUsdRate;
    public long amount;
    public long txFee;
    public String time; /* ISO 8601 format: "yyyy-MM-dd'T'HH:mm:ssZ" */

    public OrderType () {

    }
}
