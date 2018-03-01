package urbanutility.design.kaleidoscope.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by jerye on 2/16/2018.
 */

@Entity(tableName = "kaleido_deposits")
public class KaleidoDeposits {
    @PrimaryKey
    @NonNull
    String id;

    String side;
    String exchange;
    double amount;
    String symbol;
    double commission;

    public KaleidoDeposits(String id, String side, String exchange, double amount, String symbol, double commission) {
        this.id = id;
        this.side = side;
        this.exchange = exchange;
        this.amount = amount;
        this.symbol = symbol;
        this.commission = commission;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
