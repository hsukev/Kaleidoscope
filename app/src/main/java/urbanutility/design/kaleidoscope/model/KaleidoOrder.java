package urbanutility.design.kaleidoscope.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import urbanutility.design.kaleidoscope.exchange.binance.model.BinanceOrder;
import urbanutility.design.kaleidoscope.utility.KaleidoFunctions;

/**
 * Created by jerye on 1/11/2018.
 */

@Entity(tableName = "kaleido_orders")
public class KaleidoOrder {
    @PrimaryKey
    @NonNull
    public String primaryId;
    private String exchange; /* Binance, Kraken... */
    private String symbol;
    private String convertedSymbol;
    private String side; /* Buy or Sell */
    private double price;
    private double btcUsdRate;
    private double amount;
    private double btcPrice;
    private double txFee;
    private String time; /* ISO 8601 format: "yyyy-MM-dd'T'HH:mm:ssZ" */

    public KaleidoOrder() {
    }

    //Overload constructor for every exchange
    public KaleidoOrder(BinanceOrder binanceOrder, Double btcUsdRate, Double altBtcRate) {
        this.primaryId=binanceOrder.getClientOrderId();
        this.exchange = "binance";
        this.symbol = binanceOrder.getSymbol();
        this.convertedSymbol = KaleidoFunctions.convertSymbol(BaseAlts.binanceBaseAlts, this.symbol);
        this.amount = Double.parseDouble(binanceOrder.getExecutedQty());
        this.side = binanceOrder.getSide();
        this.txFee = 0.0d;
        this.price = Double.parseDouble(binanceOrder.getPrice());
        this.btcPrice = altBtcRate * this.price;
        this.time = KaleidoFunctions.convertMilliISO8601(binanceOrder.getTime());
        this.btcUsdRate = btcUsdRate;
    }

    @NonNull
    public String getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(@NonNull String primaryId) {
        this.primaryId = primaryId;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getConvertedSymbol() {
        return convertedSymbol;
    }

    public void setConvertedSymbol(String convertedSymbol) {
        this.convertedSymbol = convertedSymbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getBtcUsdRate() {
        return btcUsdRate;
    }

    public void setBtcUsdRate(double btcUsdRate) {
        this.btcUsdRate = btcUsdRate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBtcPrice() {
        return btcPrice;
    }

    public void setBtcPrice(double btcPrice) {
        this.btcPrice = btcPrice;
    }

    public double getTxFee() {
        return txFee;
    }

    public void setTxFee(double txFee) {
        this.txFee = txFee;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isBuy(){
        return this.side.equalsIgnoreCase("buy");
    }
}
