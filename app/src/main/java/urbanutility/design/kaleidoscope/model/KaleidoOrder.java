package urbanutility.design.kaleidoscope.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import urbanutility.design.kaleidoscope.model.binance.BinanceOrder;

/**
 * Created by jerye on 1/11/2018.
 */

@Entity(tableName = "kaleido_orders")
public class KaleidoOrder {
    @PrimaryKey
    @NonNull
    private String id;
    private String exchange;
    private String symbol;
    private String originalQty;
    private String executedQty;
    private String transactionFee;
    private String price;
    private long time;


    public KaleidoOrder(){
    }
    //Overload constructor for every exchange
    public KaleidoOrder(BinanceOrder binanceOrder) {
        this.id = binanceOrder.getClientOrderId();
        this.exchange = "binance";
        this.symbol = binanceOrder.getSymbol();
        this.originalQty = binanceOrder.getOrigQty();
        this.executedQty = binanceOrder.getExecutedQty();
        this.transactionFee = "0.00";
        this.price = binanceOrder.getPrice();
        this.time = binanceOrder.getTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public String getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(String transactionFee) {
        this.transactionFee = transactionFee;
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

    public String getOriginalQty() {
        return originalQty;
    }

    public void setOriginalQty(String originalQty) {
        this.originalQty = originalQty;
    }

    public String getExecutedQty() {
        return executedQty;
    }

    public void setExecutedQty(String executedQty) {
        this.executedQty = executedQty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
