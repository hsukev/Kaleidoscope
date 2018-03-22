package urbanutility.design.kaleidoscope.model;

/**
 * Created by Jason on 1/14/2018.
 */

public class KaleidoLiveMarket {
    private String symbol;
    private String exchange;
    private double price;

    public KaleidoLiveMarket(String symbol, String exchange, double price) {
        this.symbol = symbol;
        this.exchange = exchange;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
